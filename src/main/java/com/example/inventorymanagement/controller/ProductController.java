package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.ProductDTO;
import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    // ==========================================
    // HELPER METHOD: The "Filter"
    // ==========================================
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getName() // We extract just the category string here!
        );
    }

    // =========================================
    // GET: Fetch all products
    // URL: http://localhost:8080/api/products
    // Method: GET
    // =========================================
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> rawProducts = productService.getAllProducts();

        List<ProductDTO> safeProducts = rawProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(safeProducts);
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
    public ResponseEntity<ProductDTO> createProduct(
            @RequestBody Product product,
            @RequestParam Long categoryId) {

        Product savedProduct = productService.createProduct(product, categoryId);
        return new ResponseEntity<>(convertToDTO(savedProduct), HttpStatus.CREATED);
    }

    // =========================================
    // PUT: Update stock (Triggers Stock Log)
    // URL: http://localhost:8080/api/products/2/stock?newQuantity=10&username=mahhia
    // Method: PUT
    // =========================================
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductDTO> updateStock(
            @PathVariable Long id,
            @RequestParam int newQuantity,
            @RequestParam String username) {

        Product updatedProduct = productService.updateStock(id, newQuantity, username);
        return ResponseEntity.ok(convertToDTO(updatedProduct));
    }

    // =========================================
    // DELETE: Remove a product by ID
    // URL: http://localhost:8080/api/products/2
    // Method: DELETE
    // =========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}