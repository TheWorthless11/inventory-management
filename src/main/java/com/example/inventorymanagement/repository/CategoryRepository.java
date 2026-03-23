package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> { /* table name(Category), primary column type(Long)*/
    Optional<Category> findByName(String name);
}
/* We are copying all the pre-written database code from Spring Boot into this file.
JpaRepository': This is the Spring Boot "Magic Wand".
         * It secretly contains all the SQL queries for save(), findAll(), findById(), delete(), etc.
 */