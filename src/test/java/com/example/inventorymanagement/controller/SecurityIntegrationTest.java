package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.config.SecurityConfig;
import com.example.inventorymanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void registerEndpoint_withoutAuth_returnsUnauthorized() throws Exception {
        Users payload = new Users();
        payload.setUsername("public_user");
        payload.setPassword("pass123");
        payload.setRole("SELLER");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registerEndpoint_withAdmin_isAllowed() throws Exception {
        Users payload = new Users();
        payload.setUsername("admin_created_user");
        payload.setPassword("pass123");
        payload.setRole("SELLER");

        Users saved = new Users();
        saved.setId(100L);
        saved.setUsername("admin_created_user");
        saved.setPassword("encoded");
        saved.setRole("SELLER");

        Mockito.when(userService.registerUser(Mockito.any(Users.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.username").value("admin_created_user"))
                .andExpect(jsonPath("$.role").value("SELLER"));
    }

    @Test
    void adminUserEndpoint_withoutAuth_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/mahhia"))
                .andExpect(status().isUnauthorized());
    }

}

