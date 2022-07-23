package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public Page<Product> getProducts(Pageable page) {
        return productService.getProducts(page);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/category/{categoryId}")
    public Page<Product> findByCategoryId(@PathVariable("categoryId") Long categoryId, Pageable page) {
        return productService.findByCategoryId(categoryId, page);
    }

    @GetMapping("/artist/{name}")
    public Page<Product> findByArtist(@PathVariable("name") String artist, Pageable page) {
        return productService.findByArtist(artist, page);
    }

    @GetMapping("/search/{query}")
    public Page<Product> findByNameContainingIgnoreCase(@PathVariable("query") String query, Pageable page) {
        return productService.findByNameContainingIgnoreCase(query, page);
    }

    @GetMapping("/range")
    public Page<Product> getRangedPricedProducts(
            @RequestParam(required = false, defaultValue = "0") BigDecimal start,
            @RequestParam(required = false, defaultValue = "10000") BigDecimal end,
            Pageable page) {
        return productService.getRangedPricedProducts(start, end, page);
    }

    @GetMapping("/most-expensive")
    public Product getMostExpensiveProductInTheCatalogue() {
        return productService.getMostExpensiveProductInTheCatalogue();
    }
}
