package com.legallens.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.legallens.backend.model.User;
import com.legallens.backend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String password, String fullName) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("User already exists");
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);


        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        // Save user to the database
        return userRepository.save(user);

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }   

}


