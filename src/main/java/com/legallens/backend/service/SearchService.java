package com.legallens.backend.service;

import com.legallens.backend.model.Embedding;
import com.legallens.backend.repository.EmbeddingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private EmbeddingRepository embeddingRepository;

    @Autowired
    private VectorService vectorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 1. Main Search Method
    public String searchSimilarChunk(Long documentId, String userQuery) {
        try {
            // A. Convert User Query to Vector
            List<Double> queryVector = vectorService.createEmbedding(userQuery);

            // B. Get all chunks for this document
            List<Embedding> chunks = embeddingRepository.findByDocumentId(documentId);
            
            String bestChunk = "No relevant information found.";
            double maxScore = -1.0;

            // C. Compare Query vs. Every Chunk (Linear Search)
            for (Embedding chunk : chunks) {
                // Parse the stored JSON string back to a List<Double>
                List<Double> chunkVector = parseVector(chunk.getVectorData());
                
                // Calculate Similarity
                double score = cosineSimilarity(queryVector, chunkVector);
                
                // Keep the winner
                if (score > maxScore) {
                    maxScore = score;
                    bestChunk = chunk.getTextChunk();
                }
            }
            
            System.out.println("Best Match Score: " + maxScore);
            return bestChunk;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error during search: " + e.getMessage();
        }
    }

    // 2. Math Formula: Cosine Similarity 🧮
    // (A . B) / (||A|| * ||B||)
    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        if (v1 == null || v2 == null || v1.size() != v2.size()) return 0.0;

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normA += Math.pow(v1.get(i), 2);
            normB += Math.pow(v2.get(i), 2);
        }

        if (normA == 0 || normB == 0) return 0.0;
        
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    // Helper: Convert String "[0.1, 0.2...]" back to List<Double>
    private List<Double> parseVector(String vectorJson) {
        try {
            return objectMapper.readValue(vectorJson, ArrayList.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
} 