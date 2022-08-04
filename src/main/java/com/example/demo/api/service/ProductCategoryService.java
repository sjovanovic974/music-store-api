package com.example.demo.api.service;

import com.example.demo.api.model.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {

    ProductCategory saveProductCategory(ProductCategory productCategory);

    Optional<ProductCategory> findByName(String categoryName);

    void deleteCategory(Long id);

    List<ProductCategory> getCategories();

    ProductCategory updateCategory(Long id, ProductCategory category);
}
