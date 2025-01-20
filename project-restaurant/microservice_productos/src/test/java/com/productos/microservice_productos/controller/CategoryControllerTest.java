package com.productos.microservice_productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productos.microservice_productos.exception.CategoryNotFoundException;
import com.productos.microservice_productos.exception.ProductNotFoundException;
import com.productos.microservice_productos.model.entity.Category;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryServiceImpl categoryService;
    private Category category, category1;
    private Product product, product1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder().id(1L).name("Entrances").build();
        category1 = Category.builder().id(2L).name("Menus").build();
        product = Product.builder()
                .id(1L)
                .name("Chicken with rice")
                .category(category1)
                .price(12.00)
                .description("This food is very delicious for lunch").build();
        product1 = Product.builder()
                .id(2L).name("Rice with banana")
                .category(category1)
                .price(12.50)
                .description("The banana is fresh and the rice is delicious.").build();
    }

    @Test
    @DisplayName("C. Get all controllers")
    void getAllCategories() throws Exception {
        // Given
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category, category1));
        // When
        ResultActions response = mockMvc.perform(get("/category").contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Entrances"));
    }

    @Test
    @DisplayName("C. Find Category by id SUCCESS")
    void categoryById() throws Exception {
        Long id = 1L;
        // Given
        when(categoryService.findById(id)).thenReturn(category);
        // When
        ResultActions result = mockMvc.perform(get("/category/"+id.toString())
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Entrances"))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("C. Find Category by id NOT FOUND")
    void categoryByIdNotFound() throws Exception {
        Long id = 5L;
        // Given
        when(categoryService.findById(id)).thenThrow(new CategoryNotFoundException("Category not Found"));
        // When
        ResultActions result = mockMvc.perform(get("/category/"+id.toString())
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Category not Found"));
    }

    @Test
    @DisplayName("C. Find products by Category id")
    void getProductsByCategoryId() throws Exception {
        Long id = 1L;
        // Given
        when(categoryService.getAllProductsByCategoryId(id)).thenReturn(Arrays.asList(product, product1));
        // When
        ResultActions result = mockMvc.perform(get("/category/products/"+id.toString())
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Chicken with rice"))
                .andExpect(jsonPath("$[1].name").value("Rice with banana"));
    }

    @Test
    @DisplayName("C. Save a Category in db")
    void saveCategory() throws Exception {
        // Given
        when(categoryService.save(category)).thenReturn(category);
        // When
        ResultActions result = mockMvc.perform(post("/category")
                .content(objectMapper.writeValueAsString(category))
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Entrances"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("C. Update Category")
    void updateCategory() throws Exception {
        Long id = 1L;
        category.setName("Entrance updated");
        // Given
        when(categoryService.updateCategory(id, category)).thenReturn(category);
        // When
        ResultActions result = mockMvc.perform(put("/category/"+id.toString())
                        .content(objectMapper.writeValueAsString(category))
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Entrance updated"));

    }

    @Test
    @DisplayName("C. Update Category NOT FOUND")
    void updateCategoryNotFound() throws Exception {
        Long id = 5L;
        // Given
        when(categoryService.updateCategory(id, category)).thenThrow(new ProductNotFoundException("Category Not Found"));
        // When
        ResultActions result = mockMvc.perform(put("/category/"+id.toString())
                .content(objectMapper.writeValueAsString(category))
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Category Not Found"));

    }

    @Test
    @DisplayName("C. delete category by id")
    void deleteCategoryById() throws Exception {
        Long id = 1L;
        // Given
        doNothing().when(categoryService).deleteById(id);
        // When
        ResultActions result = mockMvc.perform(delete("/category/"+id.toString()));
        // Then
        verify(categoryService).deleteById(id);
        result.andDo(print())
                .andExpect(status().isNoContent());
    }
}