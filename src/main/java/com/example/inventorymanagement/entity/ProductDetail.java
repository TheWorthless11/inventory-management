package com.example.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String specifications;
    @Column(nullable = false)
    private String dimensions;
    @Column(nullable = false)
    private String weight;
    @Column(nullable = true)  // Optional field
    private String description;  // Add this field

    @OneToOne(mappedBy = "productDetail")
    private Product product;
}
