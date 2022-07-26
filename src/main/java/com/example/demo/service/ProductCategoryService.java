package com.example.demo.service;

import com.example.demo.model.ProductCategory;

import java.util.Optional;

public interface ProductCategoryService {

    ProductCategory saveProductCategory(ProductCategory productCategory);

    Optional<ProductCategory> findByName(String categoryName);

    void deleteCategory(Long id);
}
