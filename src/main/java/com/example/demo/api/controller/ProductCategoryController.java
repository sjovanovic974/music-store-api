package com.example.demo.api.controller;

import com.example.demo.api.model.ProductCategory;
import com.example.demo.api.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public List<ProductCategory> getCategories() {
        return productCategoryService.getCategories();
    }

    @PostMapping
    public ProductCategory saveProductCategory(@Valid @RequestBody ProductCategory category) {
        return productCategoryService.saveProductCategory(category);
    }

    @PutMapping("/{id}")
    public ProductCategory updateCategory(@PathVariable("id") Long id, @Valid @RequestBody ProductCategory category) {
        return productCategoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteProductCategory(@PathVariable("id") Long id) {
        productCategoryService.deleteCategory(id);
    }
}
