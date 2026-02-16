package com.legallens.backend.controller;

import java.util.*;
import com.legallens.backend.model.Document;
import com.legallens.backend.model.Embedding;
import com.legallens.backend.model.User;
import com.legallens.backend.repository.DocumentRepository;
import com.legallens.backend.repository.EmbeddingRepository;
import com.legallens.backend.repository.UserRepository;
import com.legallens.backend.service.PdfService;
import com.legallens.backend.service.VectorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;   
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/docs")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private VectorService vectorService;

    @Autowired
    private EmbeddingRepository embeddingRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {

        try {
            // 1. Validation
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Empty file");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Extract text
            String extractedText = pdfService.extractText(file);

            // 3. Chunking
            List<String> chunks = pdfService.splitText(extractedText);

            // 4. Save document Metadata FIRST (We need the ID)
            Document document = new Document();
            document.setFilename(file.getOriginalFilename());
            document.setContentType(file.getContentType());
            document.setUploadDate(LocalDateTime.now());
            document.setUser(user);

            documentRepository.save(document);

            if (chunks.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("No readable text found in PDF");
            }

            System.out.println("Total Characters: " + extractedText.length());
            System.out.println("Total Chunks Created: " + chunks.size());
            System.out.println("First Chunk Preview: " + chunks.get(0));

            // 5. Loop through chunks, Vectorize, and Save
            System.out.println("--- 2. VECTORIZING & SAVING (This may take time) ---");
            
            List<Embedding> embeddingList = new ArrayList<>();
            
            for (int i = 0; i < chunks.size(); i++) {
                String chunkText = chunks.get(i);
                
                // Get Vector from Ollama
                List<Double> vector = vectorService.createEmbedding(chunkText);
                
                // Create Embedding Object
                Embedding embedding = new Embedding();
                embedding.setTextChunk(chunkText);
                embedding.setVectorData(vector.toString()); // Convert [0.1, 0.2] to String
                embedding.setDocument(document);
                
                embeddingList.add(embedding);

                // Print progress every 10 chunks so you know it's working
                if (i % 10 == 0) System.out.println("Processed chunk " + i + "/" + chunks.size());
            }

            // Save all to database
            embeddingRepository.saveAll(embeddingList);
            System.out.println("✅ All embeddings saved to Database!");

            return ResponseEntity.ok("Success! Document ID: " + document.getId() + " | Chunks Saved: " + chunks.size());

        } 
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Processing error: " + e.getMessage());
        }
    }
}
