package com.example.demo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "product_categories", uniqueConstraints = {
        @UniqueConstraint(name = "UK_category_name", columnNames = "name")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Size(min = 2, max = 4, message = "Category name must be between 2 and 4 characters!")
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;


    @OneToMany(mappedBy = "category")
    List<Product> products;
}


