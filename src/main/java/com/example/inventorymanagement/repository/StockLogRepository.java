package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.entity.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface StockLogRepository extends JpaRepository<StockLog,Long> {
}

