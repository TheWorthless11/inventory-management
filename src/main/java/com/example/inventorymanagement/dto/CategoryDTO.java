package com.example.inventorymanagement.dto;

public class CategoryDTO {
    private Long id;
    private String name;

    // 🛑 NOTICE: We completely removed the List<Product>!
    // This permanently stops the infinite loop.

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
