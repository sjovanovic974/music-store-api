package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private CategoryName name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    static enum CategoryName {
        CD, LP, DVD, BOOK
    }
}


