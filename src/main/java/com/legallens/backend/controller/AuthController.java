package com.legallens.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legallens.backend.model.User;
import com.legallens.backend.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/auth")     // Base URL: localhost:8080/api/auth
public class AuthController {
    @Autowired
    private UserService userService;


    // Endpoint: POST /api/auth/register
    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> body) {
        String name = body.get("fullName");
        String email = body.get("email");
        String password = body.get("password");

        return userService.registerUser(name, email, password);
    }

    // Endpoint: DELETE /api/auth/delete/1
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully!";
    }
}
