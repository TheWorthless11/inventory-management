package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @Test
    void addSupplier_whenNameBlank_throwsIllegalArgumentException() {
        Supplier supplier = new Supplier();
        supplier.setSupplierName(" ");

        assertThrows(IllegalArgumentException.class, () -> supplierService.addSupplier(supplier));
    }

    @Test
    void getSupplierById_whenMissing_throwsResourceNotFoundException() {
        when(supplierRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> supplierService.getSupplierById(50L));
    }

    @Test
    void deleteSupplier_whenExists_deletesById() {
        when(supplierRepository.existsById(6L)).thenReturn(true);

        supplierService.deleteSupplier(6L);

        verify(supplierRepository).deleteById(6L);
    }
}