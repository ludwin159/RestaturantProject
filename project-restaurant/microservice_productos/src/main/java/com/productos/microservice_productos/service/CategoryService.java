package com.productos.microservice_productos.service;

import com.productos.microservice_productos.model.entity.Category;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    public List<Category> getAllCategories();
    public List<Product> getAllProductsByCategoryId(Long categoryId);
    public Category findById(Long id);
    public Category save(Category category);
    public Category
    updateCategory(Long id, Category category);
    public void deleteById(Long id);
    public List<Category> getCategoryByRestaurantId(Long restaurantId);
}
