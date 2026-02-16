package com.legallens.backend.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.legallens.backend.model.Embedding;

public interface EmbeddingRepository extends JpaRepository<Embedding, Long> {
    // We will need this later to search specifically within one document
    List<Embedding> findByDocumentId(Long documentId);
}
