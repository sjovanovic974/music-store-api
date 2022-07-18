package com.example.demo.repository;

import com.example.demo.model.Artist;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        ProductCategory cd = ProductCategory.builder()
                .name(ProductCategory.CategoryName.CD)
                .build();

        ProductCategory lp = ProductCategory.builder()
                .name(ProductCategory.CategoryName.LP)
                .build();

        ProductCategory dvd = ProductCategory.builder()
                .name(ProductCategory.CategoryName.DVD)
                .build();

        ProductCategory book = ProductCategory.builder()
                .name(ProductCategory.CategoryName.BOOK)
                .build();

        Artist iron = Artist.builder()
                .name("Iron Maiden")
                .country("UK")
                .build();

        Artist metallica = Artist.builder()
                .name("Metallica")
                .country("USA")
                .build();

        Artist partibrejkers = Artist.builder()
                .name("Partibrejkers")
                .country("Serbia")
                .build();

        Product master = Product.builder()
                .name("Master of Puppets")
                .description("Third album")
                .active(true)
                .category(cd)
                .artist(metallica)
                .imageUrl("www.google.com")
                .sku("CD-000001")
                .unitPrice(new BigDecimal("15.00"))
                .unitsInStock(3)
                .build();

        Product dark = Product.builder()
                .name("Kiselo i slatko")
                .description("Seminal album")
                .active(true)
                .category(cd)
                .artist(partibrejkers)
                .imageUrl("www.google.com")
                .sku("CD-000002")
                .unitPrice(new BigDecimal("12.00"))
                .unitsInStock(5)
                .build();

        Product justice = Product.builder()
                .name("...and Justice for All")
                .description("Fourth album")
                .active(false)
                .category(cd)
                .artist(metallica)
                .imageUrl("www.google.com")
                .sku("CD-000003")
                .unitPrice(new BigDecimal("18.00"))
                .unitsInStock(0)
                .build();

        Product live = Product.builder()
                .name("Live After Death")
                .description("Eponymous live album")
                .active(true)
                .category(lp)
                .artist(iron)
                .imageUrl("www.google.com")
                .sku("LP-000001")
                .unitPrice(new BigDecimal("35.00"))
                .unitsInStock(2)
                .build();

        Product powerslave = Product.builder()
                .name("Powerslave")
                .description("Fifth album")
                .active(true)
                .category(lp)
                .artist(iron)
                .imageUrl("www.google.com")
                .sku("LP-000002")
                .unitPrice(new BigDecimal("25.00"))
                .unitsInStock(1)
                .build();

        Product parti = Product.builder()
                .name("Partibrejkers")
                .description("Debut album")
                .active(false)
                .category(lp)
                .artist(partibrejkers)
                .imageUrl("www.google.com")
                .sku("LP-000003")
                .unitPrice(new BigDecimal("35.00"))
                .unitsInStock(0)
                .build();

        Product ride = Product.builder()
                .name("Riders on the lightning")
                .description("Early Metallica")
                .active(true)
                .category(book)
                .artist(metallica)
                .imageUrl("www.google.com")
                .sku("BK-000001")
                .unitPrice(new BigDecimal("15.00"))
                .unitsInStock(2)
                .build();

        Product trooper = Product.builder()
                .name("Troopers of the metal")
                .description("Early Iron Maiden")
                .active(false)
                .category(book)
                .artist(iron)
                .imageUrl("www.google.com")
                .sku("BK-000002")
                .unitPrice(new BigDecimal("12.00"))
                .unitsInStock(0)
                .build();

        Product koncert = Product.builder()
                .name("EXIT 2022")
                .description("Partibrejkersi na EXIT Festivalu 2022.")
                .active(true)
                .category(dvd)
                .artist(partibrejkers)
                .imageUrl("www.google.com")
                .sku("DD-000001")
                .unitPrice(new BigDecimal("15.00"))
                .unitsInStock(1)
                .build();

        productRepository.saveAll(List.of(master, dark, justice, live,
                powerslave, parti, ride, trooper, koncert));
    }


    @Test
    @DisplayName("Should find all the products in a given category")
    void shouldFindProductsByCategoryId() {
        // given
        Long categoryId = 2L;
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