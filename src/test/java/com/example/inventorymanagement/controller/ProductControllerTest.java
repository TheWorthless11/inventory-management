package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(ProductController.class)
// Loads only MVC components for ProductController

@AutoConfigureMockMvc(addFilters = true)
// Configures MockMvc for HTTP request simulation
// Disables security filters so authentication does not block requests
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // Used to simulate HTTP requests (GET, POST, etc.)

    @Autowired
    private ObjectMapper objectMapper;
    // Converts Java objects into JSON format for request body

    @MockBean
    private ProductService productService;
    // Mocked service layer to isolate controller behavior

    // =========================================================
    // TEST PURPOSE:
    // Verifies that GET /api/products returns a list of products.
    // Ensures:
    // 1. Controller calls service correctly
    // 2. Response status is 200 OK
    // 3. JSON response contains expected fields and values
    // =========================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProducts_returnsDtoList() throws Exception {

        Category category = new Category();
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(1500.0);
        product.setStockQuantity(7);
        product.setCategory(category);

        // Mock service response
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));
    }

    // =========================================================
    // TEST PURPOSE:
    // Verifies that POST /api/products creates a new product.
    // Ensures:
    // 1. Request body is correctly processed
    // 2. Service method is called with correct parameters
    // 3. Response status is 201 CREATED
    // 4. JSON response contains expected values
    // =========================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_returnsCreatedDto() throws Exception {

        Category category = new Category();
        category.setName("Electronics");

        // Request payload (input from client)
        Product payload = new Product();
        payload.setName("Mouse");
        payload.setDescription("Wireless");
        payload.setPrice(30.0);
        payload.setStockQuantity(20);

        // Simulated saved object (response from service)
        Product saved = new Product();
        saved.setId(15L);
        saved.setName("Mouse");
        saved.setDescription("Wireless");
        saved.setPrice(30.0);
        saved.setStockQuantity(20);
        saved.setCategory(category);

        // Mock service behavior
        Mockito.when(productService.createProduct(Mockito.any(Product.class), Mockito.eq(3L)))
                .thenReturn(saved);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/products?categoryId=3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }
}