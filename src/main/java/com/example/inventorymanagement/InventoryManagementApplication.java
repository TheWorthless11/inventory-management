package com.example.inventorymanagement;

import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);

    }

    @Bean
    @ConditionalOnBean(UserRepository.class)
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsernameEnv = System.getenv("ADMIN_USERNAME");
            String adminUsername = (adminUsernameEnv == null || adminUsernameEnv.isBlank())
                    ? "admin"
                    : adminUsernameEnv.trim();
            String adminPassword = System.getenv("ADMIN_PASSWORD");

            // Keep local/test startup safe if Render secret is not configured.
            if (adminPassword == null || adminPassword.isBlank()) {
                System.out.println("ADMIN_PASSWORD is not set; skipping admin bootstrap.");
                return;
            }

            if (!userRepository.existsByRole("ADMIN")) {
                Users admin = new Users();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole("ADMIN");

                userRepository.save(admin);
                System.out.println("Admin user created.");
            } else {
                System.out.println("Admin already exists.");
            }
        };
    }
    // THIS IS THE TEST DRIVE CODE:
//    @Bean
//    public CommandLineRunner testDatabase(CategoryRepository categoryRepository){
//        return args -> {
//            // 1. Create a new Category
//            Category myCategory = new Category();
//            myCategory.setName("Electronics");
//
//            // 2. Save it to PostgreSQL
//            categoryRepository.save(myCategory);
//
//            // 3. Print a success message
//            System.out.println("TEST SUCCESSFUL: Saved "+myCategory.getName()+" to the database!");
//        };
//
//    }



}
