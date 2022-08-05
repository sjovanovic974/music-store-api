package com.example.demo.api.controller;

import com.example.demo.api.model.ProductCategory;
import com.example.demo.api.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            tags = {"api", "admin"},
            summary = "Gets a list of product categories",
            description ="Retrieves a list of product categories"
    )
    public List<ProductCategory> getCategories() {
        return productCategoryService.getCategories();
    }

    @PostMapping
    @Operation(
            tags = {"api", "admin"},
            summary = "Saves a new product category",
            description ="Saves a new product category. Input is validated against a number of constraints"
    )
    public ProductCategory saveProductCategory(@Valid @RequestBody ProductCategory category) {
        return productCategoryService.saveProductCategory(category);
    }

    @PutMapping("/{id}")
    @Operation(
            tags = {"api", "admin"},
            summary = "Updates a product category",
            description ="Updates a product category. Input is validated against a number of constraints",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")}
    )
    public ProductCategory updateCategory(@PathVariable("id") Long id, @Valid @RequestBody ProductCategory category) {
        return productCategoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = {"api", "admin"},
            summary = "Deletes a product category",
            description ="Removes a product category from DB",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")}
    )
    public void deleteProductCategory(@PathVariable("id") Long id) {
        productCategoryService.deleteCategory(id);
    }
}
