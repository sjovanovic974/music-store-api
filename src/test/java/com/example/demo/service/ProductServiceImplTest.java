package com.example.demo.service;

import com.example.demo.dataloader.DataLoader;
import com.example.demo.error.exceptions.CustomApiNotFoundException;
import com.example.demo.error.exceptions.SkuCannotBeUpdatedException;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCategory;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    private final List<Product> products = DataLoader.loadData();

    @Test
    @DisplayName("Should find all product by page")
    void getProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(products, pageable, products.size());

        // when
        when(productRepository.findAll(pageable)).thenReturn(productsPage);
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
        ProductCategory cd = new ProductCategory();
        cd.setName(ProductCategory.CategoryName.CD);

        Product product = new Product();
        product.setName("Arise");
        product.setCategory(cd);

        // when
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product returnedProduct = productService.saveProduct(product);

        // then
        verify(productRepository).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertThat(returnedProduct.getName()).isEqualTo(capturedProduct.getName());
    }

    @Test
    @DisplayName("Should generate proper sku value")
    void getNextSku() {
        // given
        ProductCategory dvd = new ProductCategory();
        dvd.setName(ProductCategory.CategoryName.DVD);

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
        Product product = new Product();
        product.setId(3L);
        product.setSku("CD-000001");
        product.setUnitPrice(new BigDecimal("20.00"));

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product updatedProduct = productService.updateProduct(product);

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
        Product product = new Product(); // no id it will throw an exception

        // then
        assertThatThrownBy(() -> productService.updateProduct(product))
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("Cannot update product! Product with id " + product.getId() + " was not found!");
    }

    @Test
    @DisplayName("Should throw an SkuCannotBeUpdatedException when updating the product")
    void updateProductWithSkuException() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setSku("CD-000001");

        Product productUpdated = new Product();
        productUpdated.setSku("CD-000002");

        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productUpdated));

        // then
        assertThatThrownBy(() -> productService.updateProduct(product))
                .isInstanceOf(SkuCannotBeUpdatedException.class)
                .hasMessage("You cannot update sku!");
        verify(productRepository, never()).save(any(Product.class));
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
                .isInstanceOf(CustomApiNotFoundException.class)
                .hasMessage("Cannot delete! Product with " +
                        product.getId() + " was not found!");
    }

    @Test
    @DisplayName("Should find all the products from a given category")
    void findByCategoryId() {
        // given
        Long categoryId = 2L;
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