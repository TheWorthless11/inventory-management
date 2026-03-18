package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.ProductDetail;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.ProductDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
// Enables Mockito so we can use @Mock and @InjectMocks
class ProductDetailServiceTest {

    @Mock
    private ProductDetailRepository productDetailRepository;
    // Mock repository to simulate database operations

    @InjectMocks
    private ProductDetailService productDetailService;
    // Real service with mocked dependencies injected

    // =========================================================
    // TEST PURPOSE:
    // Verifies that the service validates input and does not allow null values.
    // Ensures IllegalArgumentException is thrown when null is passed.
    // =========================================================
    @Test
    void saveDetails_whenNull_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class,
                () -> productDetailService.saveDetails(null));
    }

    // =========================================================
    // TEST PURPOSE:
    // Verifies that a valid ProductDetail object is saved correctly.
    // Ensures:
    // 1. Repository save method is called
    // 2. Returned object contains expected data
    // =========================================================
    @Test
    void saveDetails_whenValid_savesAndReturns() {

        ProductDetail detail = new ProductDetail();
        detail.setSpecifications("RGB mechanical keyboard");
        detail.setDimensions("44cm x 14cm");
        detail.setWeight("780g");

        // Simulate repository saving the object
        when(productDetailRepository.save(detail)).thenReturn(detail);

        ProductDetail result = productDetailService.saveDetails(detail);

        // Check that returned data is correct
        assertEquals("780g", result.getWeight());

        // Verify that save() was actually called
        verify(productDetailRepository).save(detail);
    }

    // =========================================================
    // TEST PURPOSE:
    // Verifies that the service correctly handles the case
    // when a ProductDetail is not found in the database.
    // Ensures ResourceNotFoundException is thrown.
    // =========================================================
    @Test
    void getDetailById_whenMissing_throwsResourceNotFoundException() {

        when(productDetailRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productDetailService.getDetailById(20L));
    }
}