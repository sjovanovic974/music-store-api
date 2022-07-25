package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

    Page<Product> getProducts(Pageable page);

    Product getProductById(Long id);

    Product saveProduct(Product product);

    Product updateProduct(Product product, Long id);

    void deleteProductById(Long id);

    Page<Product> findByCategoryId(Long categoryId, Pageable page);

    Page<Product> findByArtist(String artist, Pageable page);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable page);

    Page<Product> getRangedPricedProducts(BigDecimal start, BigDecimal end, Pageable page);

    Product getMostExpensiveProductInTheCatalogue();
}
