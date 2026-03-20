package com.example.inventorymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // PUBLIC
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ui/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ui/register").permitAll()

                        // ADMIN ONLY
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // ADMIN + SELLER
                        .requestMatchers("/api/categories/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/api/suppliers/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/api/product-details/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers("/api/logs/**").hasAnyRole("ADMIN", "SELLER")

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults()); // keep simple auth

        return http.build();
    }
}
/*
{
  "username": "admin",
  "password": "admin123",
  "role": "ADMIN"
}

{
    "username": "seller1",
    "password": "seller123",
    "role": "SELLER"
}

{
    "username": "buyer1",
    "password": "buyer123",
    "role": "BUYER"
}
*/
