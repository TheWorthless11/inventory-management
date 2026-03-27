package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.config.SecurityConfig;
import com.example.inventorymanagement.service.CategoryService;
import com.example.inventorymanagement.service.ProductService;
import com.example.inventorymanagement.service.StockLogService;
import com.example.inventorymanagement.service.SupplierService;
import com.example.inventorymanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({HomeController.class, PageController.class})
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@SuppressWarnings("unused")
class UiAuthFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private UserService userService;

    @MockBean
    private StockLogService stockLogService;

    @Test
    void loginPage_isPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void dashboard_withoutAuth_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/ui/dashboard").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void logout_withAuth_redirectsToLoginLogoutMessage() throws Exception {
        mockMvc.perform(post("/logout").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}


