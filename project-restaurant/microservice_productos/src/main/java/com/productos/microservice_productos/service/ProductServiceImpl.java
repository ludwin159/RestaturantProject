package com.productos.microservice_productos.service;

import com.productos.microservice_productos.exception.ProductNotFoundException;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.model.repository.ProductRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("The product with id: " + id + " not found!"));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(dbProduct -> {
                    dbProduct.setDescription(product.getDescription());
                    dbProduct.setName(product.getName());
                    dbProduct.setPrice(product.getPrice());
                    productRepository.save(dbProduct);
                    return dbProduct;
                }).orElseThrow(() -> new ProductNotFoundException("The Product with id: " + id+ " not found!"));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
