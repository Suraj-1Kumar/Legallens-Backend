package com.legallens.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data     // Lombok annotation to generate getters, setters, toString, etc.
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Auto-increment strategy for MySQL
    private Long id;

    @Column(nullable = false, unique = true)     // Email should be unique
    private String email;

    private String password;

    private String fullName;
    
    private String role;
}
