package com.example.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id // The @Id annotation is used to specify the primary key of the entity. In this case, we are using a Long type for the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) /*
     * The @GeneratedValue annotation is used to specify how the primary key should be generated.
     * In this case, we are using GenerationType.IDENTITY, which means that the database will automatically generate a unique value for the primary key when a new record is inserted.
     */
    private Long id;

    @Column (nullable = false)
    private String name;


    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name="product_supplier",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name="supplier_id"))
    private List<Supplier> suppliers; /*######*/

    @OneToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;


}
