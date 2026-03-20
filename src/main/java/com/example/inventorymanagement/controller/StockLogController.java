package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.StockLogDTO;
import com.example.inventorymanagement.entity.StockLog;
import com.example.inventorymanagement.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController // I am the Waiter
@RequestMapping("/api/logs") // My name tag
public class StockLogController {

    private final StockLogService stockLogService; // My connection to the Kitchen

    @Autowired
    public StockLogController(StockLogService stockLogService) {
        this.stockLogService = stockLogService;
    }

    // ==========================================
    // HELPER METHOD: The "Filter"
    // This turns a massive database log into a clean, simple receipt!
    // ==========================================
    private StockLogDTO convertToDTO(StockLog log) {
        return new StockLogDTO(
                log.getId(),
                log.getProduct().getName(),   // MAGIC: We leave the whole Product behind and just grab its name!
                log.getUser().getUsername(),  // MAGIC: We leave the whole User behind and just grab their username!
                log.getQuantityChanged(),
                log.getTransactionType(),
                log.getTimestamp()
        );
    }

    // GET: View all stock history (http://localhost:8080/api/logs)
    @GetMapping
    public ResponseEntity<List<StockLogDTO>> getAllLogs() {
        // 1. Ask the Service for the raw logs from the database
        List<StockLog> rawLogs = stockLogService.getAllLogs();

        // 2. Convert the list of raw logs into a list of safe DTOs
        // (This loop goes through every raw log, runs our convertToDTO method on it, and packs it into a new list)
        List<StockLogDTO> safeLogs = rawLogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 3. Return the beautiful, clean list to Postman!
        return ResponseEntity.ok(safeLogs);
    }
}