package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Full integration test for the CategoryController.
 * This test loads the entire Spring application context and interacts with a real database (or an in-memory one).
 * It verifies the complete flow from the controller down to the database.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rolls back database changes after each test
@ActiveProfiles("test") // Assumes you have an application-test.properties for H2 or other test DB
@WithMockUser(roles = "ADMIN") // Provide a mock user with ADMIN role for all tests in this class
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Clean up the repository before each test to ensure isolation
        categoryRepository.deleteAll();
    }

    /**
     * Tests the successful creation of a new category via a POST request.
     * It verifies:
     * 1. The HTTP response is 201 Created.
     * 2. The response body contains the correct category details.
     * 3. The category is actually saved to the database.
     */
    @Test
    void testCreateCategory_Success() throws Exception {
        // 1. Prepare the request payload
        Category newCategory = new Category();
        newCategory.setName("Electronics");

        // 2. Perform the POST request
        mockMvc.perform(post("/api/categories")
                        .with(csrf()) // Add CSRF token for security
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                // 3. Verify the HTTP response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber()) // ID should be generated
                .andExpect(jsonPath("$.name", is("Electronics")));

        // 4. Verify the data was persisted in the database
        assertEquals(1, categoryRepository.count());
        assertTrue(categoryRepository.findByName("Electronics").isPresent());
    }

    /**
     * Tests the failure case when creating a category with an invalid name (e.g., empty).
     * It verifies that the server responds with a 400 Bad Request status.
     */
    @Test
    void testCreateCategory_ValidationFailure() throws Exception {
        // 1. Prepare an invalid request payload
        Category invalidCategory = new Category();
        invalidCategory.setName(""); // Invalid name

        // 2. Perform the POST request and verify the failure
        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                // 3. Verify the HTTP response is 400 Bad Request
                .andExpect(status().isBadRequest());

        // 4. Verify that no data was saved to the database
        assertEquals(0, categoryRepository.count());
    }

    /**
     * Tests fetching all categories after one has been created.
     * It verifies:
     * 1. The HTTP response is 200 OK.
     * 2. The response body is a JSON array containing the created category.
     */
    @Test
    void testGetAllCategories_Success() throws Exception {
        // 1. Pre-populate the database with a category
        Category category = new Category();
        category.setName("Books");
        categoryRepository.save(category);

        // 2. Perform the GET request
        mockMvc.perform(get("/api/categories"))
                // 3. Verify the HTTP response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))) // Expect a list with one item
                .andExpect(jsonPath("$[0].name", is("Books")));
    }
}

