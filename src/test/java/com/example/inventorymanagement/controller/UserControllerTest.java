package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void registerUser_returnsCreatedDtoWithoutPassword() throws Exception {
        Users payload = new Users();
        payload.setUsername("newuser");
        payload.setPassword("plain123");
        payload.setRole("seller");

        Users saved = new Users();
        saved.setId(6L);
        saved.setUsername("newuser");
        saved.setPassword("encoded");
        saved.setRole("SELLER");

        Mockito.when(userService.registerUser(Mockito.any(Users.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.role").value("SELLER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getUserByUsername_returnsUserDto() throws Exception {
        Users found = new Users();
        found.setId(7L);
        found.setUsername("mahhia");
        found.setRole("ADMIN");

        Mockito.when(userService.getUserByUsername("mahhia")).thenReturn(found);

        mockMvc.perform(get("/api/users/mahhia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mahhia"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}