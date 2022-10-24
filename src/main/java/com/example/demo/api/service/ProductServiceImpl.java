package com.example.demo.api.service;

import com.example.demo.api.repository.ArtistRepository;
import com.example.demo.api.repository.ProductCategoryRepository;
import com.example.demo.api.repository.ProductRepository;
import com.example.demo.error.exceptions.CustomApiNotFoundException;
import com.example.demo.error.exceptions.CustomBadRequestException;
import com.example.demo.api.model.Artist;
import com.example.demo.api.model.Product;
import com.example.demo.api.model.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ArtistRepository artistRepository;
    private final ProductCategoryRepository productCategoryRepository;


    public ProductServiceImpl(ProductRepository productRepository, ArtistRepository artistRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    @Override
    public Page<Product> getProducts(Pageable page) {
        return productRepository.findAll(page);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Product with id " + id + " was not found!");
                    return new CustomApiNotFoundException("Product with id " + id + " was not found!");
                });
    }

    @Override
    public Product saveProduct(Product product) {

        checkForCategoryAndArtistExistenceInDB(product);

        // default sku value
        String nextSku = product.getCategory().getName() + "-000001";

        // check if there are already products in this category in DB
        Optional<Product> lastSaved = productRepository
                .findTopByCategoryOrderByIdDesc(product.getCategory());

        // if there are products in the same category in DB call function to get new sku
        if (lastSaved.isPresent()) {
            nextSku = getNextSku(product, lastSaved.get());
        }

        product.setSku(nextSku);

        return productRepository.save(product);
    }

    private void checkForCategoryAndArtistExistenceInDB(Product product) {
        // check if category already exist
        Optional<ProductCategory> category =
                productCategoryRepository.findByNameIgnoreCase(product.getCategory().getName());

        category.ifPresentOrElse(product::setCategory, () -> {
            throw new CustomBadRequestException("You must use valid category!");
        });

        // check if artist already exist
        Optional<Artist> artist = artistRepository.findByName(product.getArtist().getName());
        artist.ifPresentOrElse(product::setArtist, () -> {
            throw new CustomBadRequestException("No such artist in DB!");
        });
    }

    public String getNextSku(Product product, Product lastSaved) {

        String categoryName = product.getCategory().getName();

        //Extracting number from the sku, adding 1 for dash "-" after categoryName
        String currentSkuNumber = lastSaved.getSku().substring(categoryName.length() + 1);

        // Adding 1 to the last saved sku number, i.e. simulating increment
        Integer number = (Integer.parseInt(currentSkuNumber)) + 1;

        return categoryName + "-" + String.format("%06d", number);
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        Product productFromDB = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update product! Product with id " + id + " was not found!");
                    throw new CustomBadRequestException("Cannot update product! Product with id "
                            + id + " was not found!");
                });

        checkForCategoryAndArtistExistenceInDB(product);

        product.setId(productFromDB.getId());
        product.setSku(productFromDB.getSku());
        product.setDateCreated(productFromDB.getDateCreated());

        if (!productFromDB.getCategory().getName().equals(product.getCategory().getName())) {
            throw new CustomBadRequestException("You are not allowed to change category! " +
                    product.getCategory().getName() + " does not correspond to sku " +
                    productFromDB.getSku());
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete! Product with " + id + " was not found!");
                    throw new CustomBadRequestException("Cannot delete! Product with " +
                            id + " was not found!");
                });

        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findByCategoryId(Long categoryId, Pageable page) {
        Page<Product> products = productRepository.findByCategoryId(categoryId, page);

        if (products.getContent().size() == 0) {
            log.error("No such category found!");
            throw new CustomApiNotFoundException("No such category found!");
        }
        return products;
    }

    @Override
    public Page<Product> findByCategoryName(String categoryName, Pageable page) {
        Page<Product> products = productRepository.findByCategoryNameIgnoreCase(categoryName, page);
        if (products.getContent().size() == 0) {
            log.error("No such category found!");
            throw new CustomApiNotFoundException("No such category found!");
        }
        return products;
    }

    @Override
    public Page<Product> findByArtist(String artist, Pageable page) {
        Page<Product> products = productRepository.findByArtist(artist, page);

        if (products.getContent().size() == 0) {
            log.error("No products found for artist: " + artist + "!");
            throw new CustomApiNotFoundException("No products found for artist: " + artist + "!");
        }
        return products;
    }

    @Override
    public Page<Product> findByNameContainingIgnoreCase(String name, Pageable page) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, page);

        if (products.getContent().size() == 0) {
            log.error("No products found for " + name + "!");
            throw new CustomApiNotFoundException("No products found for " + name + "!");
        }

        return products;
    }

    @Override
    public Page<Product> getRangedPricedProducts(BigDecimal start, BigDecimal end, Pageable page) {
        Page<Product> rangedPricedProducts = productRepository.getRangedPricedProducts(start, end, page);

        if (rangedPricedProducts.getContent().size() == 0) {
            log.error("No products found in this price range!");
            throw new CustomApiNotFoundException("No products found in this price range!");
        }

        return rangedPricedProducts;
    }

    @Override
    public Product getMostExpensiveProductInTheCatalogue() {
        Product mostExpensiveProductInTheCatalogue = productRepository.getMostExpensiveProductInTheCatalogue();

        if (mostExpensiveProductInTheCatalogue == null) {
            throw new CustomApiNotFoundException("No products in Database!");
        }
        return mostExpensiveProductInTheCatalogue;
    }
}

