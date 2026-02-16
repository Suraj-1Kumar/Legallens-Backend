package com.legallens.backend.controller;

import com.legallens.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody Map<String, Object> payload) {
        
        // 1. Get Data from Request
        Long documentId = Long.valueOf(payload.get("documentId").toString());
        String query = payload.get("query").toString();

        System.out.println("User asking: " + query + " (Doc ID: " + documentId + ")");

        // 2. Perform Search
        String bestAnswer = searchService.searchSimilarChunk(documentId, query);

        return ResponseEntity.ok(bestAnswer);
    }
}