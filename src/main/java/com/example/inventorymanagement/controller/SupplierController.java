package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // =========================================
    // GET: Fetch all suppliers
    // URL: http://localhost:8080/api/suppliers
    // Method: GET
    // =========================================
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    // =========================================
    // POST: Add a new supplier
    // URL: http://localhost:8080/api/suppliers
    // Method: POST
    // =========================================
    @PostMapping
    public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier supplier) {
        // Matches your addSupplier method in SupplierService!
        Supplier savedSupplier = supplierService.addSupplier(supplier);
        return new ResponseEntity<>(savedSupplier, HttpStatus.CREATED);
    }
}
