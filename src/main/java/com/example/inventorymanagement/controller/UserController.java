package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.UserDTO;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================
    // HELPER METHOD: The "Filter"
    // This takes a raw User from the database and turns it into a safe DTO!
    // ==========================================
    private UserDTO convertToDTO(Users user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }

    // POST: Register a new user (http://localhost:8080/api/users/register)
    //{
    //    "username": "mahhia",
    //    "password": "",
    //    "role": "ADMIN"
    //}
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody Users user) {
        // 1. Save the raw user (with password) to the database
        Users savedUser = userService.registerUser(user);

        // 2. Convert the saved user into a safe DTO
        UserDTO safeUser = convertToDTO(savedUser);

        // 3. Return the safe DTO to Postman!
        return new ResponseEntity<>(safeUser, HttpStatus.CREATED);
    }

    // ==========================================
    // GET: Fetch user by username
    // URL: http://localhost:8080/api/users/mahhia
    // ==========================================
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        // 1. Find the raw user in the database
        Users foundUser = userService.getUserByUsername(username);

        // 2. Convert them to a safe DTO
        UserDTO safeUser = convertToDTO(foundUser);

        // 3. Return the safe DTO!
        return ResponseEntity.ok(safeUser);
    }
}