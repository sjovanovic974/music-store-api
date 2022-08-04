package com.example.demo.api.dto;

import com.example.demo.api.model.Artist;
import com.example.demo.api.model.Product;
import com.example.demo.api.model.ProductCategory;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;
    private String sku;

    @NotNull
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters!")
    @NotBlank
    private String name;

    @Size(max = 350, message = "Maximum allowed size for description is 350 characters!")
    private String description;

    @NotNull
    @Size(min = 3, max = 50, message = "Image url must be between 3 and 50 characters!")
    @NotBlank
    private String imageUrl;

    @NotNull
    @Min(value = 1, message = "Minimum allowed unit price is 1!")
    @Max(value = 1000, message = "Maximum allowed unit price is 1000!")
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Min(value = 0, message = "Minimum allowed value for units in stock is 0!")
    @Max(value = 100, message = "Maximum allowed value for units in stock is 100!")
    private int unitsInStock;

    @NotNull
    @Size(min = 1, max = 50, message = "Artist name must be between 1 and 50 characters!")
    @NotBlank
    private String artist;

    @NotNull
    @Size(min = 2, max = 4, message = "Category name must be between 2 and 4 characters!")
    @NotBlank
    private String category;


    public static ProductDTO convertToResponseProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setSku(product.getSku());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setActive(product.isActive());
        productDTO.setUnitsInStock(product.getUnitsInStock());
        productDTO.setArtist(product.getArtist().getName());
        productDTO.setCategory(product.getCategory().getName());

        return productDTO;
    }

    public static Product convertToProductForSave(ProductDTO input) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(input.getCategory());

        Artist artist = new Artist();
        artist.setName(input.getArtist());

        Product product = new Product();
        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setUnitPrice(input.getUnitPrice());
        product.setImageUrl(input.getImageUrl());
        product.setActive(input.isActive());
        product.setUnitsInStock(input.getUnitsInStock());
        product.setCategory(productCategory);
        product.setArtist(artist);

        return product;
    }
}
