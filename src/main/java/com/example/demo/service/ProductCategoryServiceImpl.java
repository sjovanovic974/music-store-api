package com.example.demo.service;

import com.example.demo.model.ProductCategory;
import com.example.demo.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategory saveProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public Optional<ProductCategory> findByName(String categoryName) {
        return productCategoryRepository.findByNameIgnoreCase(categoryName);
    }

    @Override
    public void deleteCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}
