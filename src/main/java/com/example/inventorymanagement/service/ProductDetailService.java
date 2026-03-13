package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.ProductDetail;
import com.example.inventorymanagement.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    @Autowired
    public ProductDetailService(ProductDetailRepository productDetailRepository){
        this.productDetailRepository=productDetailRepository;
    }
    // 1. Save new details
    public ProductDetail saveDetails(ProductDetail detail){
        return productDetailRepository.save(detail);

    }
    // 2. Find details by ID
    public  ProductDetail getDetailById(Long id){
        return productDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("Product details not found with ID: "+id));
    }


}
