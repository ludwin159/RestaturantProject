package com.productos.microservice_productos.model.repository;

import com.productos.microservice_productos.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE upper(p.name) LIKE CONCAT('%', upper(?1), '%')")
    public List<Product> findProductByName(String name);
}
