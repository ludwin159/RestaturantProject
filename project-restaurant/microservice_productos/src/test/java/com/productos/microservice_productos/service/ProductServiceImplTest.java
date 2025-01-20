package com.productos.microservice_productos.service;

import com.productos.microservice_productos.exception.ProductNotFoundException;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    private Product product, product1;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder()
                .id(1L)
                .name("Chicken with rice")
                .price(12.00)
                .description("This food is very delicious for lunch").build();
        product1 = Product.builder()
                .id(2L).name("Rice with banana")
                .price(12.50)
                .description("The banana is fresh and the rice is delicious.").build();
    }

    @Test
    @DisplayName("S. Get All products")
    void getAll() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product1));
        // When
        List<Product> dbProducts = productService.getAll();
        // Then
        assertThat(dbProducts.size()).isEqualTo(2);
        assertThat(dbProducts.getFirst().getId()).isEqualTo(1L);
        assertThat(dbProducts.getFirst().getName()).isEqualTo("Chicken with rice");
        assertThat(dbProducts.getFirst().getPrice()).isEqualTo(12.00);
    }

    @Test
    @DisplayName("S. Find products by a name")
    void findByName() {
        // Given
            when(productRepository.findProductByName("rice")).thenReturn(Arrays.asList(product, product1));
        // When
        List<Product> dbProducts = productService.findByName("rice");
        // Then
        assertThat(dbProducts.size()).isEqualTo(2);
        assertThat(dbProducts.getFirst().getName().toUpperCase()).contains("rice".toUpperCase());
        assertThat(dbProducts.get(1).getName().toUpperCase()).contains("rice".toUpperCase());
    }

    @Test
    @DisplayName("S. Find a product by its id")
    void findById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // When
        Product dbProduct = productService.findById(1L);
        // Then
        assertThat(dbProduct).isNotNull();
        assertThat(dbProduct.getId()).isEqualTo(1L);
        assertThat(dbProduct.getName()).isEqualTo("Chicken with rice");
        assertThat(dbProduct.getDescription()).isEqualTo("This food is very delicious for lunch");
        assertThat(dbProduct.getPrice()).isGreaterThan(1);
    }

    @Test
    @DisplayName("S. Not found a product by id")
    void findByIdWithProductNotFoundException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        // When Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, ()-> productService.findById(1L));
        assertThat(exception.getMessage()).contains("The product with id: 1 not found!");
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("S. Save a product successfully")
    void save() {
        // Given
        when(productRepository.save(product)).thenReturn(product);
        // When
        Product dbProduct = productService.save(product);
        // Then
        assertThat(dbProduct).isNotNull();
        assertThat(dbProduct.getId()).isEqualTo(1L);
        assertThat(dbProduct.getName()).contains("rice");
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("S. Product repeated by name")
    void saveProductRepeat() {
        // Given
        when(productRepository.save(product)).thenThrow(new DataIntegrityViolationException("Duplicate entry for product name"));
        // When
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, ()-> productService.save(product));
        // Then
        verify(productRepository).save(product);
        assertThat(exception.getMessage()).contains("Duplicate");
    }

    @Test
    @DisplayName("S. Update product success")
    void updateProduct() {
        // Given
        String newName = "Rice updated";
        double newPrice = 13.00;
        product.setName(newName);
        product.setPrice(newPrice);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // When
        Product dbProduct = productService.updateProduct(1L, product);
        // Then
        verify(productRepository).findById(1L);
        assertThat(dbProduct.getId()).isEqualTo(1L);
        assertThat(dbProduct.getName()).isEqualTo(newName);
        assertThat(dbProduct.getPrice()).isEqualTo(newPrice);
    }

    @Test
    @DisplayName("S. Error update product")
    void updateProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        // When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, ()-> productService.updateProduct(1L, product));
        // Then
        verify(productRepository).findById(1L);
        assertThat(exception.getMessage()).isEqualTo("The Product with id: 1 not found!");
    }

    @Test
    @DisplayName("S. Error update product repeat")
    void updateProductRepeated() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenThrow(new DataIntegrityViolationException("Duplicate entry for product name"));
        // When
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, ()-> productService.updateProduct(1L, product));
        // Then
        verify(productRepository).findById(1L);
        assertThat(exception.getMessage()).contains("Duplicate entry");
    }

    @Test
    @DisplayName("S. Delete product by id success")
    void deleteById() {
        Long idtoDelete = 1L;
        // Given
        doNothing().when(productRepository).deleteById(idtoDelete);
        // When
        productService.deleteById(idtoDelete);
        // Then
        verify(productRepository).deleteById(idtoDelete);
    }

    @Test
    @DisplayName("S. Error in delete product")
    void deleteProductFailed() {
        // Given
        Long idNonExisting = 4L;
        doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(idNonExisting);
        // When
        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class, ()-> productService.deleteById(idNonExisting));
        // Then
        verify(productRepository).deleteById(idNonExisting);
        assertThat(exception.getMessage()).contains("Incorrect");
    }
}