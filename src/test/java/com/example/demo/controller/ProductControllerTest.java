package com.example.demo.controller;

import com.example.demo.api.controller.ProductController;
import com.example.demo.api.dto.ProductDTO;
import com.example.demo.api.model.Artist;
import com.example.demo.api.model.Product;
import com.example.demo.api.model.ProductCategory;
import com.example.demo.api.service.ArtistService;
import com.example.demo.api.service.ProductCategoryService;
import com.example.demo.api.service.ProductService;
import com.example.demo.error.exceptions.CustomBadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductCategoryService productCategoryService;

    @MockBean
    private ArtistService artistService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Product> products;

    @BeforeEach
    void setUp() {
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
                .id(2L)
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
                .id(3L)
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
                .id(4L)
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
                .id(5L)
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
                .id(6L)
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
                .id(7L)
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
                .id(8L)
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
                .id(9L)
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
    @DisplayName("Should return all products by pages")
    void getProducts() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(products, pageable, products.size());

        int size = productsPage.getContent().size();

        // when
        when(productService.getProducts(Mockito.any(Pageable.class)))
                .thenReturn(productsPage);

        // then
        mockMvc.perform(get("/api/products")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(size)))
                .andDo(print());
    }

    @Test
    @DisplayName("Should return a products by given id")
    void getProductById() throws Exception {
        // given
        Product master = products.get(0);
        ProductDTO dtoProduct = ProductDTO.convertToResponseProductDTO(master);

        // when
        when(productService.getProductById(anyLong())).thenReturn(master);

        // then
        mockMvc.perform(get("/api/products/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(dtoProduct.getId()))
                .andExpect(jsonPath("$.name").value(dtoProduct.getName()))
                .andExpect(jsonPath("$.category").value(dtoProduct.getCategory()))
                .andExpect(jsonPath("$.artist").value(dtoProduct.getArtist()));
    }

    @Test
    @DisplayName("Should save given product")
    void saveProduct() throws Exception {
        // given
        Product product = products.get(0);
        ProductDTO input = ProductDTO.convertToResponseProductDTO(product);

        // when
        when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        // then
        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(input.getName()))
                .andExpect(jsonPath("$.category").value(input.getCategory()))
                .andExpect(jsonPath("$.active").value(input.isActive()))
                .andExpect(jsonPath("$.unitsInStock").value(input.getUnitsInStock()));
    }

    @Test
    @DisplayName("Should return a bad request")
    void saveProductBadRequest() throws Exception {
        // given
        Product product = new Product();

        // when
        when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(product);

        // then
        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Should update given product")
    void updateProduct() throws Exception {
        // given
        Product master = products.get(0);
        ProductDTO dtoProduct = ProductDTO.convertToResponseProductDTO(master);

        // when
        when(productService.updateProduct(Mockito.any(Product.class), anyLong())).thenReturn(master);

        // then
        mockMvc.perform(put("/api/products/{id}", master.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dtoProduct)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.sku").value(master.getSku()));
    }

    @Test
    @DisplayName("Should delete product by given id")
    void deleteProductById() throws Exception {
        // given
        long id = 1L;
        willDoNothing().given(productService).deleteProductById(anyLong());

        // then
        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Bad Request when deleting non-existent id")
    void deleteProductByIdBadRequest() throws Exception {
        // given
        long id = 188L;
        willThrow(CustomBadRequestException.class).given(productService)
                .deleteProductById(anyLong());

        // then
        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should find products from given category")
    void findByCategoryId() throws Exception {
        // given
        Long categoryId = 1L;
        List<Product> filteredProducts = products.stream()
                .filter(product -> Objects.equals(product.getCategory().getId(), categoryId))
                .toList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());
        Page<ProductDTO> dtoProducts = productsPage.map(ProductDTO::convertToResponseProductDTO);

        int size = dtoProducts.getContent().size();

        // when
        when(productService.findByCategoryId(anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(productsPage);

        // then
        mockMvc.perform(get("/api/products/category/id/{categoryId}", categoryId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(size)))
                .andExpect(jsonPath("$.content[0].category")
                        .value(dtoProducts.getContent().get(0).getCategory()))
                .andDo(print());
    }

    @Test
    @DisplayName("Should find products by given artist name")
    void findByArtist() throws Exception {
        // given
        String artistName = "Metallica";
        List<Product> filteredProducts = products.stream()
                .filter(product -> Objects.equals(product.getArtist().getName(), artistName))
                .toList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());
        Page<ProductDTO> dtoProducts = productsPage.map(ProductDTO::convertToResponseProductDTO);

        int size = dtoProducts.getContent().size();


        // when
        when(productService.findByArtist(Mockito.any(String.class), Mockito.any(Pageable.class))).
                thenReturn(productsPage);

        // then
        mockMvc.perform(get("/api/products/artist/{name}", artistName)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(size)))
                .andExpect(jsonPath("$.content[0].artist")
                        .value(dtoProducts.getContent().get(0).getArtist()))
                .andDo(print());
    }

    @Test
    @DisplayName("Should find products by its name using string input of variable length")
    void findByNameContainingIgnoreCase() throws Exception {
        // given
        String query = "met";
        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getName().toLowerCase()
                        .contains(query.toLowerCase()))
                .toList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        int size = productsPage.getContent().size();

        // when
        when(productService.findByNameContainingIgnoreCase(Mockito.any(String.class),
                Mockito.any(Pageable.class))).thenReturn(productsPage);

        // then
        mockMvc.perform(get("/api/products/search/{query}", query)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(size)))
                .andDo(print());
    }

    @Test
    @DisplayName("Should find product which price is between low and high inputs")
    void getRangedPricedProducts() throws Exception {
        // given
        BigDecimal low = new BigDecimal("20.00");
        BigDecimal high = new BigDecimal("30.00");

        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getUnitPrice().doubleValue() >= low.doubleValue()
                        && product.getUnitPrice().doubleValue() <= high.doubleValue()).toList();

        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productsPage = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        int size = productsPage.getContent().size();

        // when
        when(productService.getRangedPricedProducts(Mockito.any(BigDecimal.class),
                Mockito.any(BigDecimal.class), Mockito.any(Pageable.class))).thenReturn(productsPage);

        // then
        mockMvc.perform(get("/api/products/range")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(size)))
                .andExpect(jsonPath("$.content[0].unitPrice")
                        .value(greaterThanOrEqualTo(low.doubleValue())))
                .andExpect(jsonPath("$.content[0].unitPrice")
                        .value(lessThanOrEqualTo(high.doubleValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("Should find the most expensive product in the catalogue")
    void getMostExpensiveProductInTheCatalogue() throws Exception {
        // given
        Product product = products.get(2);
        BigDecimal unitPrice = new BigDecimal("99.99");
        product.setUnitPrice(unitPrice);

        ProductDTO dtoProduct = ProductDTO.convertToResponseProductDTO(product);

        // when
        when(productService.getMostExpensiveProductInTheCatalogue()).thenReturn(product);

        // then
        mockMvc.perform(get("/api/products/most-expensive")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.unitPrice").value(dtoProduct.getUnitPrice()))
                .andDo(print());
    }
}