package com.productos.microservice_productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productos.microservice_productos.exception.ProductNotFoundException;
import com.productos.microservice_productos.model.entity.Product;
import com.productos.microservice_productos.service.ProductServiceImpl;
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
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductServiceImpl productService;
    private Product product, product1;

    @BeforeEach
    public void setUp() {
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
    @DisplayName("C.Get all Products")
    void getAllProducts() throws Exception {
        // Given
        when(productService.getAll()).thenReturn(Arrays.asList(product, product1));
        // When
        ResultActions response = mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Chicken with rice"))
                .andExpect(jsonPath("$[1].name").value(product1.getName()))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("C.Get anyone Products")
    void getAnyoneProducts() throws Exception {
        // Given
        when(productService.getAll()).thenReturn(Collections.emptyList());
        // When
        ResultActions response = mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("C.Find product by name")
    void getProductByName() throws Exception {
        // Given
        when(productService.findByName("rice")).thenReturn(Arrays.asList(product1, product));
        // When
        ResultActions response = mockMvc.perform(get("/findByName/rice").contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[1].price").value(product.getPrice()))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("C. Found product by identity")
    void findProductById() throws Exception {
        // Given
        Long findId = 1L;
        when(productService.findById(findId)).thenReturn(product);
        // When
        ResultActions response = mockMvc.perform(get("/findById/".concat(findId.toString()))
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.id").value(product.getId().toString()));
    }

    @Test
    @DisplayName("C. Product not found")
    void notFoundProduct() throws Exception {
        // Given
        Long idNonExisting = 5L;
        when(productService.findById(idNonExisting)).thenThrow(new ProductNotFoundException("Product not found"));
        // When
        ResultActions response = mockMvc.perform(get("/findById/".concat(idNonExisting.toString()))
                .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Product not found"));
    }

    @Test
    @DisplayName("C. Save product successfully")
    void saveProduct() throws Exception {
        // Given
        when(productService.save(any(Product.class))).thenReturn(product);
        // When
        ResultActions response = mockMvc.perform(post("/")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId().toString()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.description").value(product.getDescription()));
    }

    @Test
    @DisplayName("C. update product success")
    void updateProduct() throws Exception {
        // Given
        String newName = "Rice with duck";
        double newPrice = 15.00;
        long idUpdate = 1L;
        Product productUpdate = product;
        productUpdate.setName(newName);
        productUpdate.setPrice(newPrice);
        when(productService.updateProduct(any(Long.class), any(Product.class))).thenReturn(product);
        // When
        ResultActions response = mockMvc.perform(put("/updateProduct/".concat(Long.toString(idUpdate)))
                        .content(objectMapper.writeValueAsString(productUpdate))
                        .contentType(MediaType.APPLICATION_JSON));
        // Then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.price").value(newPrice));
    }

    @Test
    @DisplayName("C. Update product with exception")
    void updateWithException() throws Exception {
        // Given
        long idNotExisting = 100L;
        doThrow(new ProductNotFoundException("Product with that id not exists!")).when(productService).updateProduct(any(Long.class), any(Product.class));
        // When
        ResultActions response = mockMvc.perform(put("/updateProduct/".concat(Long.toString(idNotExisting)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));
        // Then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product with that id not exists!"));
    }

    @Test
    @DisplayName("C. When I delete a product")
    void deleteProduct() throws Exception {
        // Given
        long idDeleting = 1L;
        doNothing().when(productService).deleteById(idDeleting);
        // When
        ResultActions response = mockMvc.perform(delete("/".concat(Long.toString(idDeleting))).contentType(MediaType.APPLICATION_JSON));
        // Then
        verify(productService).deleteById(idDeleting);
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("C. Delete product with exception")
    void deleteWithException() throws Exception {
        // Given
        long idNotExisting = 100L;
        doThrow(new ProductNotFoundException("Product with that id not exists!")).when(productService).deleteById(idNotExisting);
        // When
        ResultActions response = mockMvc.perform(delete("/".concat(Long.toString(idNotExisting))));
        // Then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product with that id not exists!"));
    }
}