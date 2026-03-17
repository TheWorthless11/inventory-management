package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Tells Spring this class handles web requests (http)
@RequestMapping("/api/categories") // The base URL for all methods in this class
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // =========================================
    // GET ALL CATEGORIES
    // URL: http://localhost:8080/api/categories
    // Method: GET
    // =========================================
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        // ResponseEntity allows returning: data + HTTP status
        List<Category> categories = categoryService.getAllCategories();
        // Returns 200 OK + list of categories in the response body
        return ResponseEntity.ok(categories);
    }

    // =========================================
    // CREATE A NEW CATEGORY
    // URL: http://localhost:8080/api/categories
    // Method: POST
    //json: {
    //    "name": "Appliances"
    //}
    // =========================================
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // @RequestBody tells Spring to convert the incoming JSON text into a Java Category object
        Category savedCategory = categoryService.createCategory(category);
        // Returns 201 Created + the saved category in the response body
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
}