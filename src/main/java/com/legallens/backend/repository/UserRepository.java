package com.legallens.backend.repository;

import com.legallens.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<Entity, ID Type>
public interface UserRepository extends JpaRepository<User, Long> {

    // Magic Method: Spring automatically writes the SQL for this!
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
}