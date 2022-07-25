package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UpdateProductDTO;
import com.example.demo.model.Artist;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import com.example.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Product saveProduct(@Valid @RequestBody ProductDTO product) {

        Product productEntity = Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .imageUrl(product.getImageUrl())
                .active(product.isActive())
                .unitsInStock(product.getUnitsInStock())
                .category(ProductCategory.builder()
                        .name(product.getCategory())
                        .build())
                .artist(Artist.builder()
                        .name(product.getArtist())
                        .build())
                .build();
        return productService.saveProduct(productEntity);
    }

    @PatchMapping("/{id}")
    public Product updateProduct(@Valid @RequestBody UpdateProductDTO updateProduct, @PathVariable("id") Long id) {
        Product productById = productService.getProductById(id);

        productById.setName(updateProduct.getName());
        productById.setDescription(updateProduct.getDescription());
        productById.setUnitPrice(updateProduct.getUnitPrice());
        productById.setImageUrl(updateProduct.getImageUrl());
        productById.setActive(updateProduct.isActive());
        productById.setUnitsInStock(updateProduct.getUnitsInStock());

        return productService.updateProduct(productById,productById.getId());
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
