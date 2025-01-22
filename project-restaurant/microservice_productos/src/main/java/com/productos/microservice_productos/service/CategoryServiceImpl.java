package com.productos.microservice_productos.service;

import com.productos.microservice_productos.exception.CategoryNotFoundException;
import com.productos.microservice_productos.model.entity.Category;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategoryId(Long categoryId) {
        return categoryRepository.getAllProductsByCategoryId(categoryId);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("The category with id: "+ Long.toString(id) + " not exists!")
        );
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        return categoryRepository.findById(id).map(dbCategory -> {
            dbCategory.setName(category.getName());
            if (category.getProducts() != null && !category.getProducts().isEmpty()) {
                dbCategory.getProducts().clear();
                for (Product product: category.getProducts()) {
                    product.setCategory(category);
                    dbCategory.addProduct(product);
                }
            }
            return categoryRepository.save(dbCategory);
        }).orElseThrow(() -> new CategoryNotFoundException("The category with id: " + Long.toString(id) + " not found!"));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getCategoryByRestaurantId(Long restaurantId) {
        return categoryRepository.getCategoryByRestaurantId(restaurantId);
    }
}
