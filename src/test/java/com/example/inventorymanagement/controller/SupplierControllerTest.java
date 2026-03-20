package com.example.inventorymanagement.controller;

import java.util.List;

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

import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SupplierController.class)
@AutoConfigureMockMvc(addFilters = true)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SupplierService supplierService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllSuppliers_returnsList() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setSupplierName("Global Traders");
        supplier.setContactPhone("+880123456");

        Mockito.when(supplierService.getAllSuppliers()).thenReturn(List.of(supplier));

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplierName").value("Global Traders"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addSupplier_returnsCreated() throws Exception {
        Supplier payload = new Supplier();
        payload.setSupplierName("ABC Supply");
        payload.setContactPhone("+8809999");

        Supplier saved = new Supplier();
        saved.setId(11L);
        saved.setSupplierName("ABC Supply");
        saved.setContactPhone("+8809999");

        Mockito.when(supplierService.addSupplier(Mockito.any(Supplier.class))).thenReturn(saved);

        mockMvc.perform(post("/api/suppliers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11));
    }
}