package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.ProductDetail;
import com.example.inventorymanagement.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-details")
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @Autowired
    public ProductDetailController(ProductDetailService productDetailService) {
        this.productDetailService = productDetailService;
    }

    // =========================================
    // GET: Fetch product details by ID
    // URL: http://localhost:8080/api/product-details/1
    // Method: GET
    // =========================================
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getDetailById(@PathVariable Long id) {
        // Matches your getDetailById method in ProductDetailService!
        ProductDetail detail = productDetailService.getDetailById(id);
        return ResponseEntity.ok(detail);
    }

    // =========================================
    // POST: Save new product details
    // URL: http://localhost:8080/api/product-details
    // Method: POST
    // =========================================
    @PostMapping
    public ResponseEntity<ProductDetail> saveDetails(@RequestBody ProductDetail detail) {
        // Matches your saveDetails method in ProductDetailService!
        ProductDetail savedDetail = productDetailService.saveDetails(detail);
        return new ResponseEntity<>(savedDetail, HttpStatus.CREATED);
    }
}
