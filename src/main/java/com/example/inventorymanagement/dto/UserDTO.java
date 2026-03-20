package com.example.inventorymanagement.dto;

// This is the "Cake" we serve to the internet.
// Notice it does NOT have a @Entity or @Table annotation because it doesn't go in the database!
public class UserDTO {

    private Long id;
    private String username;
    private String role;

    //  NOTICE WHAT IS MISSING:
    // 1. No password field! (Security)
    // 2. No List<StockLog>! (Prevents infinite JSON loops)

    // Empty constructor (Spring Boot needs this)
    public UserDTO() {
    }

    // Constructor to quickly build a DTO
    public UserDTO(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}