package com.legallens.backend.service;

import java.util.List;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VectorService {

    // Make sure Ollama is running!
    private static final String OLLAMA_EMBEDDING_URL = "http://localhost:11434/api/embeddings";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Double> createEmbedding(String text) {
        // 1. Prepare Request Body
        Map<String, Object> requestBody = Map.of(
                "model", "nomic-embed-text",
                "prompt", text
        );

        // 2. Prepare Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // 3. Send Request
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OLLAMA_EMBEDDING_URL,
                    request,
                    Map.class
            );

            // 4. Extract Vector (Safely)
            if (response.getBody() != null && response.getBody().containsKey("embedding")) {
                return (List<Double>) response.getBody().get("embedding");
            } else {
                throw new RuntimeException("Ollama returned empty response");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to Ollama. Is it running? Error: " + e.getMessage());
        }
    }
}