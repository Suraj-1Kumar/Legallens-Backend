package com.legallens.backend.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public SecurityConfig() {
        System.out.println(">>> SecurityConfig LOADED <<<");
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable())   // Disable CSRF for Postman/Thunder Client testing
            .authorizeHttpRequests(auth -> auth
                // Inside securityFilterChain method:
                //.requestMatchers("/api/auth/**", "/api/docs/**").permitAll()  // Allow Login/Register/Delete without token
                .anyRequest().permitAll()    // Lock everything else
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();    // Use BCrypt for password hashing
    }  

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new RuntimeException("No authentication yet");
        };
    }
}
