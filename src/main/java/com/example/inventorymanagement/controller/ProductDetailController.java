package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.ProductDetailDTO; // IMPORT DTO
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

    // ==========================================
    //  HELPER METHOD: The "Filter"
    // ==========================================
    private ProductDetailDTO convertToDTO(ProductDetail detail) {
        // If your ProductDetail has more fields, add them here!
        return new ProductDetailDTO(detail.getId(), detail.getDescription());
    }

    // ==========================================
    // GET: Fetch product details by ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getDetailById(@PathVariable Long id) {
        ProductDetail rawDetail = productDetailService.getDetailById(id);
        return ResponseEntity.ok(convertToDTO(rawDetail));
    }

    // ==========================================
    // POST: Save new product details
    // ==========================================
    @PostMapping
    public ResponseEntity<ProductDetailDTO> saveDetails(@RequestBody ProductDetail detail) {
        ProductDetail savedDetail = productDetailService.saveDetails(detail);
        return new ResponseEntity<>(convertToDTO(savedDetail), HttpStatus.CREATED);
    }
}