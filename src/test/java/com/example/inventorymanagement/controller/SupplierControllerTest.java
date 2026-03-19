package com.example.inventorymanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;

class SupplierControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private FakeSupplierService supplierService;

    @BeforeEach
    void setUp() {
        supplierService = new FakeSupplierService();
        mockMvc = MockMvcBuilders.standaloneSetup(new SupplierController(supplierService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllSuppliers_returnsList() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setSupplierName("Global Traders");
        supplier.setContactPhone("+880123456");

        supplierService.allSuppliers = List.of(supplier);

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplierName").value("Global Traders"));
    }

    @Test
    void addSupplier_returnsCreated() throws Exception {
        Supplier payload = new Supplier();
        payload.setSupplierName("ABC Supply");
        payload.setContactPhone("+8809999");

        Supplier saved = new Supplier();
        saved.setId(11L);
        saved.setSupplierName("ABC Supply");
        saved.setContactPhone("+8809999");

        supplierService.savedSupplier = saved;

        mockMvc.perform(post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11));
    }

    private static class FakeSupplierService extends SupplierService {
        private List<Supplier> allSuppliers = new ArrayList<>();
        private Supplier savedSupplier;

        FakeSupplierService() {
            super(null);
        }

        @Override
        public List<Supplier> getAllSuppliers() {
            return allSuppliers;
        }

        @Override
        public Supplier addSupplier(Supplier supplier) {
            return savedSupplier != null ? savedSupplier : supplier;
        }
    }
}