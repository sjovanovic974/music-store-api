package com.example.demo.service;

import com.example.demo.api.service.ProductServiceImpl;
import com.example.demo.error.exceptions.CustomApiNotFoundException;
import com.example.demo.error.exceptions.CustomBadRequestException;
import com.example.demo.api.model.Artist;
import com.example.demo.api.model.Product;
import com.example.demo.api.model.ProductCategory;
import com.example.demo.api.repository.ArtistRepository;
import com.example.demo.api.repository.ProductCategoryRepository;
import com.example.demo.api.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    List<Product> products;

    @BeforeEach
    public void setUp() {
        ProductCategory cd = ProductCategory.builder()
                .id(1L)
                .name("CD")
                .build();

        ProductCategory lp = ProductCategory.builder()
                .id(2L)
                .name("LP")
                .build();

        ProductCategory dvd = ProductCategory.builder()
                .id(3L)
                .name("DVD")
                .build();

        ProductCategory book = ProductCategory.builder()
                .id(4L)
                .name("BOOK")
                .build();

        Artist iron = Artist.builder()
                .id(1L)
                .name("Iron Maiden")
                .build();

        Artist metallica = Artist.builder()
                .id(2L)
                .name("Metallica")
                .build();

        Artist partibrejkers = Artist.builder()
                .id(3L)
                .name("Partibrejkers")
                .build();

        Product master = Product.builder()
                .id(1L)
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

        products = List.of(master, dark, justice, live, powerslave, parti, ride, trooper, koncert);

    }


    @Test
    @DisplayName("Should find all product by page")
    void getProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(products, pageable, products.size());

        // when
        when(productRepository.findAll(Mockito.any(Pageable.class))).thenReturn(productsPage);
        Page<Product> returnedProducts = productService.getProducts(pageable);

        // then
        assertThat(returnedProducts).isNotEmpty();
        assertThat(returnedProducts.getSize()).isEqualTo(pageable.getPageSize());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should find product by given id")
    void getProductById() {

        // given
        Long id = 2L;
        Product product = new Product();
        product.setId(id);

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Product returnedProduct = productService.getProductById(id);

        // then
        assertThat(product).isNotNull();
        assertThat(returnedProduct.getId()).isEqualTo(product.getId());
        verify(productRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw CustomApiNotFoundException if product was not found")
    void productNotFoundById() {
        // given
        Long id = 128L;

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.getProductById(id))
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("Product with id " + id + " was not found!");
    }

    @Test
    @DisplayName("Should save product to DB")
    void saveProduct() {
        // given
        ProductCategory cd = ProductCategory.builder()
                .name("CD")
                .build();

        Artist band = Artist.builder()
                .name("Sepultura")
                .build();

        Product product = Product.builder()
                .name("Arise")
                .category(cd)
                .artist(band)
                .build();

        // when
        when(productCategoryRepository.findByNameIgnoreCase(any(String.class))).thenReturn(Optional.of(cd));
        when(artistRepository.findByName(any(String.class))).thenReturn(Optional.of(band));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product returnedProduct = productService.saveProduct(product);

        // then
        verify(artistRepository).findByName(any(String.class));
        verify(productCategoryRepository).findByNameIgnoreCase(any(String.class));
        verify(productRepository).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertThat(returnedProduct.getName()).isEqualTo(capturedProduct.getName());
    }

    @Test
    @DisplayName("Should generate proper sku value")
    void getNextSku() {
        // given
        ProductCategory dvd = new ProductCategory();
        dvd.setName("DVD");

        Product lastSavedInDB = new Product();
        lastSavedInDB.setCategory(dvd);
        lastSavedInDB.setSku("DVD-000010");

        Product productToBeSaved = new Product();
        productToBeSaved.setCategory(dvd);
        productToBeSaved.setSku(null);

        // when
        String generatedSku = productService.getNextSku(productToBeSaved, lastSavedInDB);

        // then
        assertThat(generatedSku).isGreaterThan(lastSavedInDB.getSku());
    }

    @Test
    @DisplayName("Should update the product")
    void updateProduct() {
        // given
        Product product = products.get(0);
        product.setUnitPrice(new BigDecimal("20.00"));

        // when
        when(productCategoryRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Optional.ofNullable(product.getCategory()));
        when(artistRepository.findByName(anyString()))
                .thenReturn(Optional.ofNullable(product.getArtist()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product updatedProduct = productService.updateProduct(product, product.getId());

        // then
        verify(productRepository).findById(anyLong());
        verify(productRepository).save(productCaptor.capture());

        Product capturedProduct = productCaptor.getValue();
        assertThat(updatedProduct.getUnitPrice()).isEqualTo(capturedProduct.getUnitPrice());
    }

    @Test
    @DisplayName("Should throw an CustomApiNotFoundException when updating the product")
    void updateProductWithIdException() {
        // given
        Product product = products.get(0);
        product.setId(null);  // no id it will throw an exception

        // then
        assertThatThrownBy(() -> productService.updateProduct(product, product.getId()))
                .isInstanceOf(CustomBadRequestException.class)
                .hasMessage("Cannot update product! Product with id " + product.getId() + " was not found!");
    }

    @Test
    @DisplayName("Should delete product by provided id")
    void deleteProductById() {
        // given
        Long id = 2L;

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(products.get(0)));
        productService.deleteProductById(id);

        // then
        verify(productRepository).findById(anyLong());
        verify(productRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw an CustomApiNotFoundException when deleting product by provided id")
    void deleteProductByIdException() {
        // given
        Product product = new Product(); // no id it will cause an exception

        // then
        assertThatThrownBy(() -> productService.deleteProductById(product.getId()))
                .isInstanceOf(CustomBadRequestException.class)
                .hasMessage("Cannot delete! Product with " +
                        product.getId() + " was not found!");
    }

    @Test
    @DisplayName("Should find all the products from a given category")
    void findByCategoryId() {
        // given
        Long categoryId = 2L;
        ProductCategory lp = new ProductCategory();
        lp.setId(2L);
        lp.setName("LP");

        products.get(0).setCategory(lp);
        List<Product> filteredProducts = products.stream()
                .filter(product -> Objects.equals(product.getCategory().getId(), categoryId)).toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        // when
        when(productRepository.findByCategoryId(anyLong(), any(Pageable.class))).thenReturn(productsPage);
        List<Product> returnedProducts = productService.findByCategoryId(categoryId, pageable).getContent();

        // then
        assertThat(returnedProducts).isNotEmpty();
        assertThat(returnedProducts.get(0).getCategory().getId()).isEqualTo(categoryId);
        verify(productRepository).findByCategoryId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should throw CustomApiNotFoundException when trying to find the products from a given category")
    void findByCategoryIdException() {
        // given
        Long categoryId = 22L;
        List<Product> filteredProducts = products.stream()
                .filter(product -> Objects.equals(product.getCategory().getId(), categoryId)).toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        when(productRepository.findByCategoryId(anyLong(), any(Pageable.class)))
                .thenReturn(productsPage);

        // then
        assertThatThrownBy(
                () -> productService.findByCategoryId(categoryId, pageable))
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("No such category found!");
    }

    @Test
    @DisplayName("Should find all products for a given artist")
    void findByArtist() {
        // given
        String artistName = "Metallica";
        List<Product> filteredProducts = products.stream()
                .filter(product -> Objects.equals(product.getArtist().getName(), artistName)).toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        // when
        when(productRepository.findByArtist(anyString(), any(Pageable.class))).thenReturn(productsPage);
        List<Product> returnedProducts = productService.findByArtist(artistName, pageable).getContent();

        // then
        assertThat(returnedProducts).isNotEmpty();
        assertThat(returnedProducts.get(0).getArtist().getName()).isEqualTo(artistName);
        verify(productRepository).findByArtist(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should throw CustomApiNotFoundException when trying to find the products by artist name")
    void findByArtistException() {
        // given
        String artistName = "Pixies";
        List<Product> filteredProducts = products.stream()
                .filter(product -> Objects.equals(product.getArtist().getName(), artistName)).toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        when(productRepository.findByArtist(anyString(), any(Pageable.class)))
                .thenReturn(productsPage);

        // then
        assertThatThrownBy(
                () -> productService.findByArtist(artistName, pageable))
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("No products found for artist: " + artistName + "!");
    }

    @Test
    @DisplayName("Should find products by its name using string input of variable length")
    void findByNameContainingIgnoreCase() {
        // given
        String input = "power";
        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(input.toLowerCase())).toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        // when
        when(productRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(productsPage);
        List<Product> returnedProducts = productService
                .findByNameContainingIgnoreCase(input, pageable).getContent();

        // then
        assertThat(returnedProducts).isNotEmpty();
        assertThat(returnedProducts.get(0).getName()).containsIgnoringCase(input);
        verify(productRepository).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should throw an CustomApiNotFoundException when searching for products by name " +
            "using given input string")
    void findByNameContainingIgnoreCaseException() {
        // given
        String input = "Bleach";
        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(input.toLowerCase())).toList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        // when
        when(productRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(productsPage);

        // then
        assertThatThrownBy(() -> productService.findByNameContainingIgnoreCase(input, pageable))
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("No products found for " + input + "!");
    }

    @Test
    @DisplayName("Should find product which price is between low and high inputs")
    void getRangedPricedProducts() {
        // given
        BigDecimal low = new BigDecimal("20.00");
        BigDecimal high = new BigDecimal("30.00");

        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getUnitPrice().doubleValue() >= low.doubleValue()
                        && product.getUnitPrice().doubleValue() <= high.doubleValue()).toList();

        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        // when
        when(productRepository.getRangedPricedProducts(any(BigDecimal.class),
                any(BigDecimal.class), any(Pageable.class))).thenReturn(productsPage);

        List<Product> returnedProducts = productService
                .getRangedPricedProducts(low, high, pageable).getContent();

        // then
        assertThat(returnedProducts).isNotEmpty();
        assertThat(returnedProducts.get(0).getUnitPrice().doubleValue())
                .isBetween(low.doubleValue(), high.doubleValue());
        verify(productRepository).getRangedPricedProducts(
                any(BigDecimal.class), any(BigDecimal.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should through an CustomApiNotFoundException if there is no product found")
    void getRangedPricedProductsException() {
        // given
        BigDecimal low = new BigDecimal("40.00");
        BigDecimal high = new BigDecimal("50.00");

        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getUnitPrice().doubleValue() >= low.doubleValue()
                        && product.getUnitPrice().doubleValue() <= high.doubleValue()).toList();

        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        // when
        when(productRepository.getRangedPricedProducts(any(BigDecimal.class),
                any(BigDecimal.class), any(Pageable.class))).thenReturn(productsPage);

        // then
        assertThatThrownBy(() -> productService.getRangedPricedProducts(low, high, pageable))
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("No products found in this price range!");
    }

    @Test
    @DisplayName("Should find the most expensive product in the catalogue")
    void getMostExpensiveProductInTheCatalogue() {
        // given
        Product mostExpensiveProduct = products.stream().max(Comparator.comparing(Product::getUnitPrice)).get();

        // when
        when(productRepository.getMostExpensiveProductInTheCatalogue()).thenReturn(mostExpensiveProduct);
        Product returnedProduct = productService.getMostExpensiveProductInTheCatalogue();

        // then
        assertThat(returnedProduct.getUnitPrice()).isEqualTo(mostExpensiveProduct.getUnitPrice());
        verify(productRepository).getMostExpensiveProductInTheCatalogue();
    }

    @Test
    @DisplayName("Should throw an CustomApiNotFoundException when trying to " +
            "find the most expensive product in the catalogue")
    void getMostExpensiveProductInTheCatalogueException() {

        // when
        when(productRepository.getMostExpensiveProductInTheCatalogue()).thenReturn(null);

        // then
        assertThatThrownBy(() -> productService.getMostExpensiveProductInTheCatalogue())
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("No products in Database!");
    }
}