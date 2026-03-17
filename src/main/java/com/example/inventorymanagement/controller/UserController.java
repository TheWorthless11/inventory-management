package com.example.inventorymanagement.controller;

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

    // POST: Register a new user (http://localhost:8080/api/users/register)
    //{
    //    "username": "mahhia",
    //    "password": "",
    //    "role": "ADMIN"
    //}
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        // NOTE: Make sure "registerUser" perfectly matches the method name you wrote in your UserService!
        Users savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}