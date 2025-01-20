package com.productos.microservice_productos.model.repository;

import com.productos.microservice_productos.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

class ProductRepositoryTest {
    private Product product, product1;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void beforeEach() {
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
    @DisplayName("Find Product x ID")
    public void findProductById() {
        // Given
        String productName = "Chicken with rice";
        String productDescription = "This food is very delicious for lunch";
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // When
        Optional<Product> optProduct = productRepository.findById(1L);
        if (optProduct.isPresent()) {
            Product dbProduct = optProduct.get();
            // Then
            assertThat(dbProduct).isNotNull();
            assertThat(dbProduct.getName()).isEqualTo(productName);
            assertThat(dbProduct.getDescription()).isEqualTo(productDescription);
        }
    }
    @Test
    @DisplayName("When I find a product by its id, but that doesn't found")
    public void findByIdButDoesntfound() {
        // Given
        when(productRepository.findById(3L)).thenReturn(Optional.empty());
        // When
        Optional<Product> dbProduct = productRepository.findById(3L);
        // Then
        assertThat(dbProduct).isEmpty();
    }

    @Test
    @DisplayName("Find product x name")
    public void findProductByName() {
        // Given
        List<Product> productList = Arrays.asList(product, product1);
        when(productRepository.findProductByName("rice")).thenReturn(productList);
        // When
        List<Product> dbProducts = productRepository.findProductByName("rice");
        // Then
        assertThat(dbProducts).isNotNull();
        assertThat(dbProducts.size()).isGreaterThan(1);
        assertThat(dbProducts.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("Get all Products")
    public void getAllProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product1));
        // When
        List<Product> listProducts = productRepository.findAll();
        // Then
        assertThat(listProducts).isNotNull();
        assertThat(listProducts.size()).isEqualTo(2);
        assertThat(listProducts.getFirst().getId()).isEqualTo(1L);
        assertThat(listProducts.getFirst().getName()).isEqualTo("Chicken with rice");
        assertThat(listProducts.get(1).getName()).isEqualTo("Rice with banana");
    }
    @Test
    @DisplayName("Save a product")
    public void saveProduct() {
        // Given
        when(productRepository.save(product)).thenReturn(product);
        // When
        Product dbProduct = productRepository.save(product);
        // Then
        assertThat(dbProduct).isNotNull();
        assertThat(dbProduct.getId()).isEqualTo(1L);
        assertThat(dbProduct.getName()).isEqualTo("Chicken with rice");
    }
    @Test
    @DisplayName("Save a product when that already exists")
    public void saveProductExist() {
        // Given
        Product productRepeat = Product.builder().name(product.getName()).price(12.03).description("This is another rice").build();
        // When
        when(productRepository.save(productRepeat)).thenThrow(new DataIntegrityViolationException("Duplicate entry for product name"));
        // Then
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(productRepeat));
        assertThat(exception.getMessage()).contains("Duplicate entry");
    }

    @Test
    @DisplayName("Delete product")
    public void deleteProduct() {
        // Given
        Long idForDelete = 1L;
        doNothing().when(productRepository).deleteById(idForDelete);
        // When
        productRepository.deleteById(idForDelete);
        // Then
        verify(productRepository).deleteById(idForDelete);
    }
    @Test
    @DisplayName("Delete product when that not exists")
    public void deleteNonExistingProduct() {
        // Given
        Long nonExistingId = 4L;
        doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(nonExistingId);
        // When Then
        assertThrows(EmptyResultDataAccessException.class, () -> productRepository.deleteById(nonExistingId));
    }
    @Test
    @DisplayName("Update Product")
    public void updateProduct() {
        // Given
        double newPrice = 50.00;
        product.setPrice(newPrice);
        when(productRepository.save(product)).thenReturn(product);
        // When
        Product productUpdated = productRepository.save(product);
        // Then
        assertThat(productUpdated.getPrice()).isEqualTo(newPrice);
    }
}
