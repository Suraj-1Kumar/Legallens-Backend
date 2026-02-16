package com.legallens.backend.service;

import java.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfService {
    public String extractText(MultipartFile file){
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            // Create the text stripper (the tool that reads text)
            PDFTextStripper stripper = new PDFTextStripper();

            // Extract text from the PDF document
            String text = stripper.getText(document);

            return text.trim();
        
        } catch (Exception e) {
            throw new RuntimeException("Failed in extracting text from PDF", e);
        }
    }

    // --- NEW METHOD: The Butcher Knife ---
    public List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        int chunkSize = 1000; // Characters per chunk
        int overlap = 200;    // Overlap to keep context

        for (int i = 0; i < text.length(); i += (chunkSize - overlap)) {
            // Calculate the end index safely
            int end = Math.min(text.length(), i + chunkSize);
            
            // Cut the chunk
            String chunk = text.substring(i, end);
            chunks.add(chunk);
        }
        
        return chunks;
    }
}
