package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.CategoryDTO; // 📦 IMPORT DTO
import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN','SELLER','BUYER')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedCategory));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER','BUYER')")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(convertToDTO(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}