package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // =========================================
    // GET: Fetch all products
    // URL: http://localhost:8080/api/products
    // Method: GET
    // =========================================
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // =========================================
    // POST: Create a new product
    // URL: http://localhost:8080/api/products?categoryId=6
    // Method: POST
    //{
    //    "name": "Oak Dining Table",
    //    "description": "Solid wood dining table",
    //    "price": 450.00,
    //    "stockQuantity": 5
    //}
    // =========================================
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product,
            @RequestParam Long categoryId) {
        Product savedProduct = productService.createProduct(product, categoryId);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // =========================================
    // PUT: Update stock (Triggers Stock Log)
    // URL: http://localhost:8080/api/products/2/stock?newQuantity=10&username=mahhia
    // Method: PUT
    // =========================================
    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long id,
            @RequestParam int newQuantity,
            @RequestParam String username) {

        Product updatedProduct = productService.updateStock(id, newQuantity, username);
        return ResponseEntity.ok(updatedProduct);
    }

    // =========================================
    // DELETE: Remove a product by ID
    // URL: http://localhost:8080/api/products/2
    // Method: DELETE
    // =========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // Returns a 204 No Content status
    }
}