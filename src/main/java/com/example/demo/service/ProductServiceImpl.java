package com.example.demo.service;

import com.example.demo.error.exceptions.CustomApiNotFoundException;
import com.example.demo.error.exceptions.SkuCannotBeUpdatedException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
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

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        // not allowing external setting of sku value
        product.setSku(null);

        // default sku value
        String nextSku = product.getCategory().getName() + "-000001";

        // check if there are already products in this category in DB
        Optional<Product> lastSaved = productRepository.findTopByCategoryOrderByIdDesc(product.getCategory());

        // if there are products in the same category in DB call function to get new sku
        if (lastSaved.isPresent()) {
            nextSku = getNextSku(product, lastSaved.get());
        }

        product.setSku(nextSku);

        return productRepository.save(product);
    }

    public String getNextSku(Product product, Product lastSaved) {

        String nextSku = "";
        String categoryName = product.getCategory().getName().toString();

        //Extracting number from the sku, adding 1 for dash "-" after categoryName
        String currentSkuNumber = lastSaved.getSku().substring(categoryName.length() + 1);

        // Adding 1 to the last saved sku number, i.e. simulating increment
        Integer number = (Integer.parseInt(currentSkuNumber)) + 1;

        nextSku = categoryName + "-" + String.format("%06d", number);

        return nextSku;
    }

    @Override
    public Product updateProduct(Product product) {
        Product productFromDB = productRepository.findById(product.getId())
                .orElseThrow(() -> {
                    log.error("Cannot update product! Product with id " + product.getId() + " was not found!");
                    return new CustomApiNotFoundException("Cannot update product! Product with id "
                            + product.getId() + " was not found!");
                });

        if (!productFromDB.getSku().equals(product.getSku())) {
            throw new SkuCannotBeUpdatedException("You cannot update sku!");
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete! Product with " + id + " was not found!");
                    return new CustomApiNotFoundException("Cannot delete! Product with " +
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

