package com.example.inventorymanagement.dto;

import java.time.LocalDateTime;

public class StockLogDTO {

    private Long id;
    private String productName; // Notice we use String, NOT the Product entity!
    private String username;    // Notice we use String, NOT the Users entity!
    private int quantityChanged;
    private String transactionType;
    private LocalDateTime timestamp;

    // Empty constructor
    public StockLogDTO() {}

    // Full constructor
    public StockLogDTO(Long id, String productName, String username, int quantityChanged, String transactionType, LocalDateTime timestamp) {
        this.id = id;
        this.productName = productName;
        this.username = username;
        this.quantityChanged = quantityChanged;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getQuantityChanged() { return quantityChanged; }
    public void setQuantityChanged(int quantityChanged) { this.quantityChanged = quantityChanged; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}