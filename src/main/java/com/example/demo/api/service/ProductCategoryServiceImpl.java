package com.example.demo.api.service;

import com.example.demo.api.repository.ProductCategoryRepository;
import com.example.demo.error.exceptions.CustomBadRequestException;
import com.example.demo.api.model.ProductCategory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    @Override
    public List<ProductCategory> getCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategory updateCategory(Long id, ProductCategory category) {
        Optional<ProductCategory> dbCategory = productCategoryRepository.findById(id);
        if (dbCategory.isEmpty()) {
            throw new CustomBadRequestException("No product with id: " + id  + " in system!");
        }
        dbCategory.get().setName(category.getName());
        return productCategoryRepository.save(dbCategory.get());
    }

}
