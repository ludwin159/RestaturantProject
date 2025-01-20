package com.productos.microservice_productos.model.repository;

import com.productos.microservice_productos.model.entity.Category;
import com.productos.microservice_productos.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT p FROM Product p WHERE p.category = ?1")
    public List<Product> getAllProductsByCategoryId(Long idCategory);
}
