package com.example.demo.controller;

import com.example.demo.model.ProductCategory;
import com.example.demo.service.ProductCategoryService;
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
        ProductCategory productCategory = ProductCategory.builder()
                .name(category.getName())
                .build();

        return productCategoryService.saveProductCategory(productCategory);
    }

    @PutMapping("/{id}")
    public ProductCategory updateCategory(@PathVariable("id") Long id, @RequestBody ProductCategory category) {
        return productCategoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteProductCategory(@PathVariable("id") Long id) {
        productCategoryService.deleteCategory(id);
    }
}
