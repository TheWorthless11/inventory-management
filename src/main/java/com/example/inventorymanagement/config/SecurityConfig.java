package com.example.inventorymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC: Anyone can register or see products
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/products/**").permitAll()

                        // 2. ADMIN ONLY: User management
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // 3. INVENTORY MANAGEMENT: Admin and Sellers
                        // This covers all the specific paths you mentioned!
                        .requestMatchers("/api/categories/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/api/product-details/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/api/stock-logs/**").hasAnyRole("ADMIN", "SELLER")

                        // 4. CATCH-ALL: Everything else requires at least a login
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }
}

/*
{
  "username": "admin",
  "password": "admin123",
  "role": "ADMIN"
}
*/
