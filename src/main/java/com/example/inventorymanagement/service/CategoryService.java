package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
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

    // ==========================================
    //   EXCEPTION HANDLING  BELOW
    // ==========================================

    // 3. Fetch a single Category by its ID
    public Category getCategoryById(Long id) {
        // We ask the repository to find the category by its ID.
        // If it exists, it returns it.
        // If it does NOT exist, we trigger our custom ResourceNotFoundException!
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " was not found in the database!"));
        // Our GlobalExceptionHandler catches this and returns a clean 404 Not Found JSON.
    }

    // 4. Delete a category
    public void deleteCategory(Long id) {
        // First, we check if the ID actually exists in the database.
        if (!categoryRepository.existsById(id)) {
            // If it doesn't exist, we can't delete it! Throw our custom exception.
            throw new ResourceNotFoundException("Cannot delete: Category with ID " + id + " does not exist!");
        }
        // If it DOES exist, we go ahead and delete it.
        categoryRepository.deleteById(id);
    }

    // 5. Update an existing category
    public Category updateCategory(Long id, Category updatedCategory) {
        if (updatedCategory.getName() == null || updatedCategory.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty!");
        }

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " was not found in the database!"));

        existingCategory.setName(updatedCategory.getName().trim());
        return categoryRepository.save(existingCategory);
    }



}
