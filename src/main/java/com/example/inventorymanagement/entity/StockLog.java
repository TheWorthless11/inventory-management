package com.example.inventorymanagement.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer quantityChanged; // e.g., +10 or -5
    @Column(nullable = false)
    private String transactionType;   // e.g., "PURCHASE", "RESTOCK", "SALE"
    private LocalDateTime timestamp;  // When it happened

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "product_id") /*add column name product_id*/
    private Product product;

}
