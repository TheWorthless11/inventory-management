package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
        }
)
@AutoConfigureMockMvc(addFilters = false)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SupplierService supplierService;

    @Test
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11));
    }
}