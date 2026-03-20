package com.example.inventorymanagement.dto;

public class SupplierDTO {
    private Long id;
    private String supplierName;

    // 🛑 NOTICE: If your Supplier entity has a List<Product>, we leave it out here!

    // Empty Constructor
    public SupplierDTO() {}

    // Full Constructor
    public SupplierDTO(Long id, String supplierName) {
        this.id = id;
        this.supplierName = supplierName;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}