package com.example.inventorymanagement;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);

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
