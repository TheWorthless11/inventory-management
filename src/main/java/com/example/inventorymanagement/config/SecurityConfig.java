package com.example.inventorymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean // 1. This creates the BCrypt tool we need for the UserService!
    public PasswordEncoder passwordEncoder() { // PasswordEncoder This is just an interface
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder is one implementation of that interface.
    }

    //that code is basically disabling Spring Security for now so you can test your API easily.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth.anyRequest().permitAll());
        return http.build();

    }

}
