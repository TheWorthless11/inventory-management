package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.StockLog;
import com.example.inventorymanagement.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // I am the Waiter
@RequestMapping("/api/logs") // My name tag
public class StockLogController {

    private final StockLogService stockLogService; // My connection to the Kitchen

    @Autowired
    public StockLogController(StockLogService stockLogService) {
        this.stockLogService = stockLogService;
    }

    // GET: View all stock history (http://localhost:8080/api/logs)
    @GetMapping
    public List<StockLog> getAllLogs() {
        // The Waiter asks the Kitchen for the logs, then hands them to Postman!
        return stockLogService.getAllLogs();
    }
}