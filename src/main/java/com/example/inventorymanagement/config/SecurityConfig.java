package com.example.inventorymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                        .requestMatchers("/", "/error", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // READ ACCESS RULES
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "SELLER", "BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyRole("ADMIN", "SELLER", "BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/product-details/**").hasAnyRole("ADMIN", "SELLER", "BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/suppliers/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/logs/**").hasRole("ADMIN")

                        // UI PAGES REQUIRE LOGIN
                        .requestMatchers("/ui/**").authenticated()

                        // ALL OTHER API CALLS REQUIRE LOGIN (fine-grained role checks via @PreAuthorize)
                        .requestMatchers("/api/**").authenticated()

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/ui/dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
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
