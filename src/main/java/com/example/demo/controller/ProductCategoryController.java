package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.ProductCategory;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @PostMapping
    public ProductCategory saveProductCategory(@Valid @RequestBody CategoryDTO category) {
        ProductCategory productCategory = ProductCategory.builder()
                .name(category.getName())
                .build();

        return productCategoryService.saveProductCategory(productCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteProductCategory(@PathVariable("id") Long id) {
        productCategoryService.deleteCategory(id);
    }
}
