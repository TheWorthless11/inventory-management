package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.exception.ResourceNotFoundException; // 📦 IMPORT YOUR CUSTOM EXCEPTION
import com.example.inventorymanagement.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // ==========================================
    // 1. Get all suppliers
    // ==========================================
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // ==========================================
    // 2. Add a new supplier
    // ==========================================
    public Supplier addSupplier(Supplier supplier) {
        // SAFETY CHECK: Use getSupplierName() instead of getName()
        // If the name is null or empty, it throws an IllegalArgumentException.
        // Our GlobalExceptionHandler will automatically catch this and return a polite 400 Bad Request!
        if (supplier.getSupplierName() == null || supplier.getSupplierName().trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name cannot be empty.");
        }
        return supplierRepository.save(supplier);
    }

    // ==========================================
    // ADDED JUST IN CASE NEED THEM LATER
    // ==========================================

    // 3. Get a single supplier by ID
    public Supplier getSupplierById(Long id) {
        // If the supplier doesn't exist, this throws our custom exception.
        // The GlobalExceptionHandler catches it and returns a 404 Not Found!
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with ID " + id + " not found in the database!"));
    }

    // 4. Update a supplier by ID
    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        if (updatedSupplier.getSupplierName() == null || updatedSupplier.getSupplierName().trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name cannot be empty.");
        }
        if (updatedSupplier.getContactPhone() == null || updatedSupplier.getContactPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier phone cannot be empty.");
        }

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with ID " + id + " not found in the database!"));

        existingSupplier.setSupplierName(updatedSupplier.getSupplierName().trim());
        existingSupplier.setContactPhone(updatedSupplier.getContactPhone().trim());

        return supplierRepository.save(existingSupplier);
    }

    // 5. Delete a supplier by ID
    public void deleteSupplier(Long id) {
        // Check if the supplier exists before trying to delete
        if (!supplierRepository.existsById(id)) {
            // Prevents deleting a ghost supplier and returns a clean 404!
            throw new ResourceNotFoundException("Cannot delete: Supplier with ID " + id + " does not exist!");
        }
        supplierRepository.deleteById(id);
    }
}