package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.ProductDetail;
import com.example.inventorymanagement.exception.ResourceNotFoundException; // IMPORT YOUR CUSTOM EXCEPTION
import com.example.inventorymanagement.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    @Autowired
    public ProductDetailService(ProductDetailRepository productDetailRepository){
        this.productDetailRepository = productDetailRepository;
    }

    // ==========================================
    // 1. Save new details
    // ==========================================
    public ProductDetail saveDetails(ProductDetail detail){
        // SAFETY CHECK: Prevent saving if the user sends an empty JSON body
        if (detail == null) {
            // This throws an IllegalArgumentException.
            // Our GlobalExceptionHandler catches it and returns a 400 Bad Request!
            throw new IllegalArgumentException("Product details cannot be empty or null.");
        }

        return productDetailRepository.save(detail);
    }

    // ==========================================
    // 2. Find details by ID
    // ==========================================
    public ProductDetail getDetailById(Long id){
        // We replaced the generic "RuntimeException" with our specific "ResourceNotFoundException"
        // Now, if the database cannot find this ID, it cleanly throws a 404 Not Found error!
        return productDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product details not found with ID: " + id));
    }
}