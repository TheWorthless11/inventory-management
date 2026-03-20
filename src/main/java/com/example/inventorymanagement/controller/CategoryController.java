package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.CategoryDTO; // 📦 IMPORT DTO
import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ==========================================
    // 🪄 HELPER METHOD: The "Filter"
    // ==========================================
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    // ==========================================
    // GET: View all categories
    // URL: http://localhost:8080/api/categories
    // ==========================================
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> rawCategories = categoryService.getAllCategories();

        // Convert the raw list into a safe DTO list
        List<CategoryDTO> safeCategories = rawCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(safeCategories);
    }

    // ==========================================
    // POST: Create a new category
    // URL: http://localhost:8080/api/categories
    // ==========================================
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCategory));
    }
}