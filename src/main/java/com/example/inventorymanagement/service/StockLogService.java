package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.StockLog;
import com.example.inventorymanagement.entity.Users;
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

    // This method is called by the ProductService whenever stock changes!
    public StockLog recordLog(Product product, Users user, int quantityChanged, String transactionType) {
        StockLog log = new StockLog();
        log.setProduct(product);
        log.setUser(user); // The person who made the change
        log.setQuantityChanged(quantityChanged);
        log.setTransactionType(transactionType); // e.g., "RESTOCK" or "SALE"
        log.setTimestamp(LocalDateTime.now()); // Records the exact current time

        return stockLogRepository.save(log);
    }

    public List<StockLog> getAllLogs() {
        return stockLogRepository.findAll();
    }
}
