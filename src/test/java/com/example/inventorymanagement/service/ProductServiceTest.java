package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
// 👉 Enables Mockito in JUnit 5 (so @Mock works)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    // 👉 Fake version of ProductRepository (no real DB)

    @Mock
    private CategoryRepository categoryRepository;
    // 👉 Fake category DB

    @Mock
    private StockLogService stockLogService;
    // 👉 Fake logging service (we just verify calls)

    @Mock
    private UserService userService;
    // 👉 Fake user service

    @InjectMocks
    private ProductService productService;
    // 👉 Real ProductService but with mocked dependencies injected

    // =========================================================
    // TEST 1: Invalid price
    // =========================================================
    @Test
    void createProduct_whenPriceNegative_throwsIllegalArgumentException() {

        Product product = new Product();
        product.setPrice(-1.0); //  invalid price
        product.setStockQuantity(10);

        // 👉 Expect exception when method is called
        assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct(product, 1L));
    }

    // =========================================================
    // TEST 2: Category not found
    // =========================================================
    @Test
    void createProduct_whenCategoryMissing_throwsResourceNotFoundException() {

        Product product = new Product();
        product.setPrice(100.0);
        product.setStockQuantity(5);

        // 👉 Simulate category NOT found in DB
        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createProduct(product, 99L));
    }

    // =========================================================
    // TEST 3: Successful product creation
    // =========================================================
    @Test
    void createProduct_whenValid_setsCategoryAndSaves() {

        Product product = new Product();
        product.setName("Mouse");
        product.setPrice(25.0);
        product.setStockQuantity(8);

        Category category = new Category();
        category.setId(3L);
        category.setName("Accessories");

        // 👉 Mock DB behavior
        when(categoryRepository.findById(3L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(product))
                .thenReturn(product);

        // 👉 Call real method
        Product result = productService.createProduct(product, 3L);

        // 👉 Assertions
        assertEquals(category, result.getCategory());
        verify(productRepository).save(product); // ensure save called
    }

    // =========================================================
    // TEST 4: Invalid stock update
    // =========================================================
    @Test
    void updateStock_whenNegativeQuantity_throwsIllegalArgumentException() {

        Product existing = new Product();
        existing.setId(10L);
        existing.setStockQuantity(5);

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(existing));

        // 👉 Expect exception for invalid quantity
        assertThrows(IllegalArgumentException.class,
                () -> productService.updateStock(10L, -1, "mahhia"));
    }

    // =========================================================
    // TEST 5: Successful stock update (RESTOCK)
    // =========================================================
    @Test
    void updateStock_whenRestock_recordsLogAndReturnsUpdatedProduct() {

        Product existing = new Product();
        existing.setId(10L);
        existing.setStockQuantity(5);

        Users actor = new Users();
        actor.setUsername("mahhia");

        // 👉 Mock DB + service behavior
        when(productRepository.findById(10L))
                .thenReturn(Optional.of(existing));

        when(productRepository.save(existing))
                .thenReturn(existing);

        when(userService.getUserByUsername("mahhia"))
                .thenReturn(actor);

        // 👉 Call method (increase stock)
        Product updated = productService.updateStock(10L, 12, "mahhia");

        // 👉 Check new value
        assertEquals(12, updated.getStockQuantity());

        // 👉 Verify log created correctly
        verify(stockLogService).recordLog(
                eq(existing),
                eq(actor),
                eq(7),          // difference (12 - 5)
                eq("RESTOCK")
        );

        verify(productRepository).save(existing);
    }
}