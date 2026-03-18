package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.StockLog;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.service.StockLogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockLogController.class)
// Loads only MVC components needed for StockLogController

@AutoConfigureMockMvc(addFilters = false)
// Enables MockMvc and disables security filters for testing
class StockLogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // Used to simulate HTTP requests

    @MockBean
    private StockLogService stockLogService;
    // Mocked service layer to isolate controller behavior

    // =========================================================
    // TEST PURPOSE:
    // Verifies that GET /api/logs returns a list of stock logs.
    // Ensures:
    // 1. Controller correctly calls service layer
    // 2. Response status is 200 OK
    // 3. Entity data is properly transformed into DTO fields
    //    (productName, username, transactionType)
    // =========================================================
    @Test
    void getAllLogs_returnsSafeDtoList() throws Exception {

        // Create test data for product
        Product product = new Product();
        product.setName("Keyboard");

        // Create test data for user
        Users user = new Users();
        user.setUsername("mahhia");

        // Create stock log entity
        StockLog log = new StockLog();
        log.setId(1L);
        log.setProduct(product);
        log.setUser(user);
        log.setQuantityChanged(4);
        log.setTransactionType("RESTOCK");
        log.setTimestamp(LocalDateTime.of(2026, 3, 18, 10, 30));

        // Mock service response
        Mockito.when(stockLogService.getAllLogs()).thenReturn(List.of(log));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Keyboard"))
                .andExpect(jsonPath("$[0].username").value("mahhia"))
                .andExpect(jsonPath("$[0].transactionType").value("RESTOCK"));
    }
}