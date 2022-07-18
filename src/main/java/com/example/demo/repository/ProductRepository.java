package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable page);

    // JPQL
    @Query("select p from Product p where p.artist.name = ?1")
    Page<Product> findByArtist(String artist, Pageable page);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable page);

    // JPQL
    @Query("select p from Product p where p.unitPrice >= :start and p.unitPrice <= :end")
    Page<Product> getRangedPricedProducts(
            @Param("start") BigDecimal start, @Param("end") BigDecimal end, Pageable page);

    // Native Query
    @Query(value =
            "SELECT * FROM products ORDER BY unit_price DESC LIMIT 1",
            nativeQuery = true)
    Product getMostExpensiveProductInTheCatalogue();
}
