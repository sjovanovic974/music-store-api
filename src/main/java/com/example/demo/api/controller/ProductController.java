package com.example.demo.api.controller;

import com.example.demo.api.dto.ProductDTO;
import com.example.demo.api.model.Product;
import com.example.demo.api.service.ProductService;
import com.example.demo.error.exceptions.CustomApiNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.media.MediaType;
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
    @Operation(
            tags = {"api", "user"},
            summary = "Gets all the products",
            description = "Retrieves product from DB using DTO and pages"
    )
    public Page<ProductDTO> getProducts(Pageable page) {
        return productService.getProducts(page).map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/{id}")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a single product",
            description = "Retrieves product from DB by provided id",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")},
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDTO.class)),
                            description = "Successful response"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(examples = @ExampleObject(value = "\"status\": 400,\n" +
                                    "\"message\": \"You must use valid category!\",\n" +
                                    "\"path\": \"/api/products\",\n" +
                                    "\"timeStamp\": \"2022-08-05 11:27:40\"")),
                            description = "Not found exception")}
    )
    public ProductDTO getProductById(@PathVariable("id") Long id) {
        return ProductDTO.convertToResponseProductDTO(productService.getProductById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            tags = {"api", "admin"},
            summary = "Saves a single product",
            description = "Saves a product to DB. " +
                    "Input is defined by a DTO and it is validated against set of constraints"
    )
    public ProductDTO saveProduct(@Valid @RequestBody ProductDTO product) {
        Product savedProduct = productService.saveProduct(ProductDTO.convertToProductForSave(product));
        return ProductDTO.convertToResponseProductDTO(savedProduct);
    }

    @PutMapping("/{id}")
    @Operation(
            tags = {"api", "admin"},
            summary = "Updates a single product",
            description = "Updates a product selected by provided id. " +
                    "Input is defined by a DTO and it is validated against set of constraints",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")}
    )
    public ProductDTO updateProduct(
            @Valid @RequestBody ProductDTO updateProduct, @PathVariable("id") Long id) {
        Product updatedProduct = productService.updateProduct(ProductDTO.convertToProductForSave(updateProduct), id);
        return ProductDTO.convertToResponseProductDTO(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = {"api", "admin"},
            summary = "Deletes a single product from DB",
            description = "Deletes a single product by provided id",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")}
    )
    public void deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/category/id/{categoryId}")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a page of products by a category",
            description = "Retrieves a page of products by a category that is identified by provided category id",
            parameters = {@Parameter(name = "categoryId", description = "Valid categoryId number", example = "3")}
    )
    public Page<ProductDTO> findByCategoryId(
            @PathVariable("categoryId") Long categoryId, Pageable page) {
        return productService.findByCategoryId(categoryId, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/category/name/{categoryName}")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a page of products by a category",
            description = "Retrieves a page of products by a category that is identified by provided category name",
            parameters = {@Parameter(name = "categoryName", description = "Valid category name", example = "CD")})
    public Page<ProductDTO> findByCategoryName(
            @PathVariable("categoryName") String categoryName, Pageable page) {
        return productService.findByCategoryName(categoryName, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/artist/{name}")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a page of products by an atist",
            description = "Retrieves a a page of products by an artist name",
            parameters = {@Parameter(name = "name", description = "Valid artist name", example = "Metallica")}
    )
    public Page<ProductDTO> findByArtist(@PathVariable("name") String artist, Pageable page) {

        return productService.findByArtist(artist, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/search/{query}")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a page of products by a query",
            description = "Retrieves a page of products by a query. Searches through product name field." +
                    "Input query is case insensitive.",
            parameters = {@Parameter(name = "query", description = "String of characters", example = "met")}
    )
    public Page<ProductDTO> findByNameContainingIgnoreCase(
            @PathVariable("query") String query, Pageable page) {

        return productService.findByNameContainingIgnoreCase(query, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/range")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a page of products between a given price range",
            description = "Retrieves a page of products between a given price range. Params have default values " +
                    "so that this endpoint returns the page in any case.",
            parameters = {@Parameter(name = "start", description = "Starting price range, inclusive", example = "10.00"),
                    @Parameter(name = "end", description = "Ending price range, inclusive", example = "30.00")}
    )
    public Page<ProductDTO> getRangedPricedProducts(
            @RequestParam(required = false, defaultValue = "0") BigDecimal start,
            @RequestParam(required = false, defaultValue = "10000") BigDecimal end,
            Pageable page) {
        return productService.getRangedPricedProducts(start, end, page)
                .map(ProductDTO::convertToResponseProductDTO);
    }

    @GetMapping("/most-expensive")
    @Operation(
            tags = {"api", "user"},
            summary = "Gets a single most expansive product in DB",
            description = "Retrieves a single most expansive product"
    )
    public ProductDTO getMostExpensiveProductInTheCatalogue() {
        return ProductDTO.convertToResponseProductDTO(
                productService.getMostExpensiveProductInTheCatalogue());
    }
}
