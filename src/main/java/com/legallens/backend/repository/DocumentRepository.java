package com.legallens.backend.repository;

import com.legallens.backend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    // Find all documents uploaded by a specific user
    List<Document> findByUserId(Long userId);
}