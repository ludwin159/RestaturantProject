package com.productos.microservice_productos.service;

import com.productos.microservice_productos.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public List<Product> getAll();
    public List<Product> findByName(String name);
    public Product findById(Long id);
    public Product save(Product product);
    public Product updateProduct(Long id, Product product);
    public void deleteById(Long id);
}
