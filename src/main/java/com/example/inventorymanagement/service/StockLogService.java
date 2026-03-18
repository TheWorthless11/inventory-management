package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.StockLog;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.repository.StockLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockLogService {

    private final StockLogRepository stockLogRepository;

    @Autowired
    public StockLogService(StockLogRepository stockLogRepository) {
        this.stockLogRepository = stockLogRepository;
    }

    // ==========================================
    // 1. Record a stock change (Called by ProductService)
    // ==========================================
    public StockLog recordLog(Product product, Users user, int quantityChanged, String transactionType) {

        // SAFETY CHECKS (Defensive Programming)
        // If any of these are triggered, our GlobalExceptionHandler catches the
        // IllegalArgumentException and sends a polite 400 Bad Request error.

        if (product == null) {
            throw new IllegalArgumentException("Cannot record log: Product is missing!");
        }
        if (user == null) {
            throw new IllegalArgumentException("Cannot record log: User is missing!");
        }
        if (quantityChanged < 0) {
            throw new IllegalArgumentException("Cannot record log: Quantity changed cannot be negative!");
        }
        if (transactionType == null || transactionType.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot record log: Transaction type (SALE/RESTOCK) is required!");
        }

        // If all checks pass, build the log and save it!
        StockLog log = new StockLog();
        log.setProduct(product);
        log.setUser(user); // The person who made the change
        log.setQuantityChanged(quantityChanged);
        log.setTransactionType(transactionType); // e.g., "RESTOCK" or "SALE"
        log.setTimestamp(LocalDateTime.now()); // Records the exact current time

        return stockLogRepository.save(log);
    }

    // ==========================================
    // 2. Fetch all logs
    // ==========================================
    public List<StockLog> getAllLogs() {
        return stockLogRepository.findAll();
    }
}