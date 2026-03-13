package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Tells Spring Boot this is a "Chef" (Business Logic class)
public class CategoryService {
    // The Service needs the Repository to do its job
    private final CategoryRepository categoryRepository;

    @Autowired // Spring Boot automatically connects the Repository to this Service
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    // --- BUSINESS LOGIC METHODS ---
// 1. Get all categories to show on the frontend

    public List<Category> getAllCategories(){
        return  categoryRepository.findAll();
    }

    // 2. Add a brand new category
    public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty!");
        }
        return categoryRepository.save(category);
    }

}
