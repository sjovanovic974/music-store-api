package com.example.demo.dataloader;

import com.example.demo.model.Artist;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import com.example.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadData(productRepository);
    }

    static void loadData(ProductRepository productRepository) {
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

        productRepository.saveAll(List.of(master, dark, justice, live, powerslave, parti, ride, trooper, koncert));
    }
}