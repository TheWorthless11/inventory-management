package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.ProductDetail;
import com.example.inventorymanagement.service.ProductDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductDetailController.class)
// Loads only MVC components for ProductDetailController

@AutoConfigureMockMvc(addFilters = true)
// Enables MockMvc and disables security filters
class ProductDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // Used to simulate HTTP requests

    @Autowired
    private ObjectMapper objectMapper;
    // Converts Java objects to JSON for request body

    @MockBean
    private ProductDetailService productDetailService;
    // Mocked service to isolate controller logic

    // =========================================================
    // TEST PURPOSE:
    // Verifies that GET /api/product-details/{id} returns a product detail.
    // Ensures:
    // 1. Controller calls service correctly
    // 2. Response status is 200 OK
    // 3. Response JSON contains correct fields and values
    // =========================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void getDetailById_returnsDetail() throws Exception {

        ProductDetail detail = new ProductDetail();
        detail.setId(2L);
        detail.setSpecifications("Mechanical switches");
        detail.setDimensions("44x14cm");
        detail.setWeight("700g");
        detail.setDescription("Mechanical switches");

        // Mock service response
        Mockito.when(productDetailService.getDetailById(2L)).thenReturn(detail);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/product-details/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.description").value("Mechanical switches"));
    }

    // =========================================================
    // TEST PURPOSE:
    // Verifies that POST /api/product-details creates a new record.
    // Ensures:
    // 1. Request body is correctly processed
    // 2. Service method is invoked
    // 3. Response status is 201 CREATED
    // 4. Response contains generated ID
    // =========================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void saveDetails_returnsCreated() throws Exception {

        // Request payload
        ProductDetail payload = new ProductDetail();
        payload.setSpecifications("Aluminum body");
        payload.setDimensions("20x10cm");
        payload.setWeight("300g");

        // Simulated saved object
        ProductDetail saved = new ProductDetail();
        saved.setId(8L);
        saved.setSpecifications("Aluminum body");
        saved.setDimensions("20x10cm");
        saved.setWeight("300g");
        saved.setDescription("Aluminum body");

        // Mock service behavior
        Mockito.when(productDetailService.saveDetails(Mockito.any(ProductDetail.class)))
                .thenReturn(saved);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/product-details")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.description").value("Aluminum body"));
    }
}