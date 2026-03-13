package com.example.inventorymanagement.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) /* If you delete a Category: Then all its Products will also be deleted automatically.
    here category is the obj created in Product entity Category category */
    private List<Product> products;
}

/*
ManyToOne → owns the foreign key
OneToMany → inverse side (mappedBy)
Always write ManyToOne first, because that side owns the relationship.
 */
