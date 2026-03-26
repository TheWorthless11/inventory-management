package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.StockLog;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.repository.StockLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
// Enables Mockito so that @Mock and @InjectMocks work correctly
class StockLogServiceTest {

    @Mock
    private StockLogRepository stockLogRepository;
    // Mock repository to simulate database behavior

    @InjectMocks
    private StockLogService stockLogService;
    // Real service instance with mocked repository injected

    // =========================================================
    // TEST PURPOSE:
    // Verifies that the service validates input properly.
    // Ensures that a null product is not allowed and triggers an exception.
    // =========================================================
    @Test
    void recordLog_whenProductMissing_throwsIllegalArgumentException() {

        Users user = new Users();

        assertThrows(IllegalArgumentException.class,
                () -> stockLogService.recordLog(null, user, 2, "SALE"));
    }

    // =========================================================
    // TEST PURPOSE:
    // Verifies that a valid stock log is created and saved correctly.
    // Ensures:
    // 1. Fields are set properly (quantity, type, product)
    // 2. Timestamp is generated
    // 3. Repository save method is called
    // =========================================================
    @Test
    void recordLog_whenValid_savesLogWithTimestamp() {

        Product product = new Product();
        Users user = new Users();

        // Simulate repository save returning the same object
        when(stockLogRepository.save(any(StockLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockLog result = stockLogService.recordLog(product, user, 5, "RESTOCK");

        // Validate field values
        assertEquals(5, result.getQuantityChanged());
        assertEquals("RESTOCK", result.getTransactionType());
        assertEquals(product, result.getProduct());

        // Ensure timestamp is assigned
        assertNotNull(result.getTimestamp());

        // Verify repository interaction
        verify(stockLogRepository).save(any(StockLog.class));
    }

    // =========================================================
    // TEST PURPOSE:
    // Verifies that the service correctly retrieves all stock logs
    // from the repository without modification.
    // =========================================================
    @Test
    void getAllLogs_returnsRepositoryData() {

        StockLog log = new StockLog();

        // Mock repository returning a list with one log
        when(stockLogRepository.findAll()).thenReturn(List.of(log));

        List<StockLog> logs = stockLogService.getAllLogs();

        // Validate returned data
        assertEquals(1, logs.size());
    }
}