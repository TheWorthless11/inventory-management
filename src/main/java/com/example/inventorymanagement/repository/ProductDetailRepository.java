package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
}
// Empty! Spring Boot handles all the basic CRUD operations for ProductsDetails through this interface. If we need custom queries later, we can add them here.
