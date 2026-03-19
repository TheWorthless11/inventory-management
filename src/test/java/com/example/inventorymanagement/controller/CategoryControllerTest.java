package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

// These imports are the magic "Asserts" that check the robot's results
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc; // THIS IS  INVISIBLE ROBOT!

    @MockBean
    private CategoryService categoryService; // A "fake" service so we don't accidentally write to the real database during tests

    @Autowired
    private ObjectMapper objectMapper; // A tool that converts Java objects into JSON text

    // ==========================================
    // TEST 1: Can our API return a list of categories?
    // ==========================================
    @Test
    public void testGetAllCategories_ShouldReturnList() throws Exception {
        // 1. SETUP: Train the fake service
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Electronics");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Furniture");

        List<Category> fakeList = Arrays.asList(cat1, cat2);

        // Tell the fake service: "When the controller asks for categories, give it this fake list!"
        Mockito.when(categoryService.getAllCategories()).thenReturn(fakeList);

        // 2. ACT & ASSERT: Let the robot do its job!
        mockMvc.perform(get("/api/categories")) // Send a GET request to /api/categories
                .andExpect(status().isOk()) // Expect a 200 OK status
                .andExpect(jsonPath("$[0].name").value("Electronics")) // Expect the 1st item's name to be "Electronics"
                .andExpect(jsonPath("$[1].name").value("Furniture"));  // Expect the 2nd item's name to be "Furniture"
    }

    // ==========================================
    // TEST 2: Can our API create a new category?
    // ==========================================
    @Test
    public void testCreateCategory_ShouldReturnCreated() throws Exception {
        // 1. SETUP: Create the category we want to send
        Category newCategory = new Category();
        newCategory.setName("Books");

        Category savedCategory = new Category();
        savedCategory.setId(3L);
        savedCategory.setName("Books");

        // Tell the fake service: "When the controller tries to save 'Books', return it with ID 3!"
        Mockito.when(categoryService.createCategory(Mockito.any(Category.class))).thenReturn(savedCategory);

        // 2. ACT & ASSERT: Let the robot do its job!
        mockMvc.perform(post("/api/categories") // Send a POST request
                        .contentType(MediaType.APPLICATION_JSON) // Tell the server we are sending JSON
                        .content(objectMapper.writeValueAsString(newCategory))) // Convert our Java 'newCategory' into actual JSON text
                .andExpect(status().isCreated()) // Expect a 201 CREATED status
                .andExpect(jsonPath("$.id").value(3)) // Expect the database to have assigned ID 3
                .andExpect(jsonPath("$.name").value("Books")); // Expect the name to be "Books"
    }

    @Test
    public void testCreateCategory_WithInvalidPayload_ShouldReturnBadRequest() throws Exception {
        Category invalidCategory = new Category();

        Mockito.when(categoryService.createCategory(Mockito.any(Category.class)))
                .thenThrow(new IllegalArgumentException("Category name cannot be empty!"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/categories"));
    }
}