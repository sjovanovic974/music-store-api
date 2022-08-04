package com.example.demo.repository;

import com.example.demo.api.model.Product;
import com.example.demo.api.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should find all the products in a given category")
    void shouldFindProductsByCategoryId() {
        // given
        Long categoryId = 1L;
        Pageable page = PageRequest.of(0, 3);

        // when
        List<Product> productsByCategoryId = productRepository.findByCategoryId(categoryId, page).getContent();

        // then
        assertThat(productsByCategoryId).isNotEmpty();
        assertThat(productsByCategoryId.get(0).getCategory().getId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("Should find all the products for a given artist")
    void shouldFindProductsByArtist() {
        // given
        String artistName = "Metallica";
        Pageable page = PageRequest.of(0, 3);

        // when
        List<Product> productsByArtistName = productRepository.findByArtist(artistName, page).getContent();

        // then
        assertThat(productsByArtistName).isNotEmpty();
        assertThat(productsByArtistName.get(0).getArtist().getName()).isEqualTo(artistName);
    }

    @Test
    @DisplayName("Should find all the products for a given product name or just a part of it")
    void shouldFindProductsByProductName() {
        // given
        String productName = "Power";
        Pageable page = PageRequest.of(0, 3);

        // when
        List<Product> ProductsByNameContaining = productRepository.findByNameContainingIgnoreCase(
                productName, page).getContent();

        // then
        assertThat(ProductsByNameContaining).isNotEmpty();
        assertThat(ProductsByNameContaining.get(0).getName()).containsIgnoringCase(productName);
    }

    @Test
    @DisplayName("Should find all the products between provided price range inclusive")
    void shouldFindRangePricedProduct() {
        // given
        BigDecimal low = new BigDecimal("15.00");
        BigDecimal high = new BigDecimal("25.00");
        Pageable page = PageRequest.of(0, 6);

        // when
        List<Product> productWithCertainPriceRange =
                productRepository.getRangedPricedProducts(low, high, page).getContent();

        assertThat(productWithCertainPriceRange).isNotEmpty();
        assertThat(productWithCertainPriceRange.get(0).getUnitPrice()).isBetween(low, high);
    }

    @Test
    void getMostExpensiveProductInTheCatalogue() {
        // given
        BigDecimal price = new BigDecimal("35.00");

        // when
        Product mostExpensiveProduct = productRepository.getMostExpensiveProductInTheCatalogue();

        // then
        assertThat(mostExpensiveProduct.getUnitPrice()).isEqualTo(price);
    }
}