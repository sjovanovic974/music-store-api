package com.example.demo.service;

import com.example.demo.model.ProductCategory;

public interface ProductCategoryService {

    ProductCategory saveProductCategory(ProductCategory productCategory);

    void deleteCategory(Long id);
}
