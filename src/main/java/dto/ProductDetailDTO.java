package com.example.inventorymanagement.dto;

public class ProductDetailDTO {
    private Long id;
    private String description; // Add any other fields your entity has (like manufacturer, etc.)

    public ProductDetailDTO() {}

    public ProductDetailDTO(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}