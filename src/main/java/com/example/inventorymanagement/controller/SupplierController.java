package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.dto.SupplierDTO; // 📦 IMPORT DTO
import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ==========================================
    // 🪄 HELPER METHOD: The "Filter"
    // ==========================================
    private SupplierDTO convertToDTO(Supplier supplier) {
        return new SupplierDTO(supplier.getId(), supplier.getSupplierName());
    }

    // ==========================================
    // GET: Fetch all suppliers
    // ==========================================
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<Supplier> rawSuppliers = supplierService.getAllSuppliers();

        List<SupplierDTO> safeSuppliers = rawSuppliers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(safeSuppliers);
    }

    // ==========================================
    // POST: Add a new supplier
    // ==========================================
    @PostMapping
    public ResponseEntity<SupplierDTO> addSupplier(@RequestBody Supplier supplier) {
        Supplier savedSupplier = supplierService.addSupplier(supplier);
        return new ResponseEntity<>(convertToDTO(savedSupplier), HttpStatus.CREATED);
    }
}