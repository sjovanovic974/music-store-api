package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
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
    public Page<ProductDTO> getProducts(Pageable page) {
        return productService.getProducts(page).map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable("id") Long id) {
        return ProductDTO.convertToResponseProductDTO(productService.getProductById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO saveProduct(@Valid @RequestBody ProductDTO product) {
        Product savedProduct = productService.saveProduct(ProductDTO.convertToProductForSave(product));
        return ProductDTO.convertToResponseProductDTO(savedProduct);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(
            @Valid @RequestBody ProductDTO updateProduct, @PathVariable("id") Long id) {
        Product updatedProduct = productService.updateProduct(ProductDTO.convertToProductForSave(updateProduct), id);
        return ProductDTO.convertToResponseProductDTO(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/category/id/{categoryId}")
    public Page<ProductDTO> findByCategoryId(
            @PathVariable("categoryId") Long categoryId, Pageable page) {
        return productService.findByCategoryId(categoryId, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/category/name/{categoryName}")
    public Page<ProductDTO> findByCategoryName(
            @PathVariable("categoryName") String categoryName, Pageable page) {
        return productService.findByCategoryName(categoryName, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/artist/{name}")
    public Page<ProductDTO> findByArtist(@PathVariable("name") String artist, Pageable page) {
        return productService.findByArtist(artist, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/search/{query}")
    public Page<ProductDTO> findByNameContainingIgnoreCase(
            @PathVariable("query") String query, Pageable page) {
        return productService.findByNameContainingIgnoreCase(query, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/range")
    public Page<ProductDTO> getRangedPricedProducts(
            @RequestParam(required = false, defaultValue = "0") BigDecimal start,
            @RequestParam(required = false, defaultValue = "10000") BigDecimal end,
            Pageable page) {
        return productService.getRangedPricedProducts(start, end, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/most-expensive")
    public ProductDTO getMostExpensiveProductInTheCatalogue() {
        return ProductDTO.convertToResponseProductDTO(
                productService.getMostExpensiveProductInTheCatalogue());
    }
}
