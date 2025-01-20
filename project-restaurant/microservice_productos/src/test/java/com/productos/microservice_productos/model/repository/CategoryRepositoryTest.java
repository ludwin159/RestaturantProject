package com.productos.microservice_productos.model.repository;

import com.productos.microservice_productos.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


class CategoryRepositoryTest {
    Product product, product1;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product
                .builder()
                .name("Coca cola")
                .description("This is a great soda.")
                .price(1.5).build();
        product1 = Product
                .builder()
                .name("Triple cola")
                .description("These sodas aren't so good!.")
                .price(0.5).build();
    }

    @Test
    @DisplayName("R. Get Products by their categories.")
    void getAllProductsByCategoryId() {
        // Given
        Long id = 1L;
        when(categoryRepository.getAllProductsByCategoryId(id)).thenReturn(Arrays.asList(product, product1));
        // When
        List<Product> productsByCategoryId = categoryRepository.getAllProductsByCategoryId(id);
        // Then
        assertThat(productsByCategoryId).isNotNull();
        assertThat(productsByCategoryId.size()).isEqualTo(2);
    }
}