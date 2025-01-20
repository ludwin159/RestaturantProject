package com.productos.microservice_productos.service;

import com.productos.microservice_productos.exception.CategoryNotFoundException;
import com.productos.microservice_productos.model.entity.Category;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.model.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryServiceImplTest {
    private Category category, category1;
    private Product product, product1;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder().id(1L).name("Entrances").build();
        category1 = Category.builder().id(2L).name("Menus").build();
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
    @DisplayName("S. Get all categories")
    void getAllCategories() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category, category1));
        // When
        List<Category> categories = categoryService.getAllCategories();
        // Then
        assertThat(categories).isNotNull();
        assertThat(categories.size()).isEqualTo(2);
        assertThat(categories.getFirst().getName()).isEqualTo("Entrances");
    }

    @Test
    @DisplayName("S. find Products by category id")
    void getAllProductsByCategoryId() {
        Long categoryId = 1L;
        // Given
        when(categoryRepository.getAllProductsByCategoryId(categoryId))
                .thenReturn(Arrays.asList(product, product1));
        // When
        List<Product> productsByCategory = categoryService.getAllProductsByCategoryId(categoryId);
        // Then
        assertThat(productsByCategory).isNotNull();
        assertThat(productsByCategory.size()).isEqualTo(2);
        assertThat(productsByCategory.getFirst().getName()).isEqualTo("Coca cola");
    }

    @Test
    @DisplayName("S. Find Category by id")
    void findById() {
        Long categoryId = 1L;
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // When
        Category getCategory = categoryService.findById(categoryId);
        // Then
        assertThat(getCategory).isNotNull();
        assertThat(getCategory.getName()).isEqualTo("Entrances");
    }

    @Test
    @DisplayName("S. Not found Category")
    void findByIdNotFound() {
        Long categoryId = 1L;
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // When Then
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class, () -> categoryService.findById(categoryId));
        assertThat(exception.getMessage())
                .isEqualTo("The category with id: "+ Long.toString(categoryId) + " not exists!");
    }

    @Test
    @DisplayName("S. Save a category")
    void save() {
        // Given
        when(categoryRepository.save(category)).thenReturn(category);
        // When
        Category categorySaved = categoryService.save(category);
        // Then
        assertThat(categorySaved).isNotNull();
        assertThat(categorySaved.getId()).isEqualTo(1L);
        assertThat(categorySaved.getName()).isEqualTo("Entrances");
    }

    @Test
    @DisplayName("S. Update Category successfully")
    void updateCategory() {
        Long id = 1L;
        // Given
        String nameUpdate = "Soups";
        category.setName(nameUpdate);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        // When
        Category categoryUpdate = categoryService.updateCategory(id, category);
        // Then
        assertThat(categoryUpdate).isNotNull();
        assertThat(categoryUpdate.getName()).isEqualTo("Soups");
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("S. Update Category Not found")
    void updateCategoryNotFound() {
        Long id = 1L;
        // Given
        String nameUpdate = "Soups";
        category.setName(nameUpdate);
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        // When
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(id, category));
        // Then
        assertThat(exception.getMessage()).isEqualTo("The category with id: " + Long.toString(id) + " not found!");
    }

    @Test
    @DisplayName("S. delete category")
    void deleteById() {
        Long id = 1L;
        // Given
        doNothing().when(categoryRepository).deleteById(id);
        // When
        categoryService.deleteById(id);
        // Then
        verify(categoryRepository).deleteById(id);
    }
}