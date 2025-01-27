package com.restaurants.microservice_restaurants.controller;

import com.commons.CategoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import com.restaurants.microservice_restaurants.service.RestaurantService;
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


import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@WebMvcTest
class RestaurantControllerTest {

    private Restaurant restaurant1, restaurant2;
    private CategoryDTO category;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurant1 = Restaurant.builder()
                .id(1L)
                .ruc("20756902101")
                .tradeName("Julio's Restaurant").build();
        restaurant2 = Restaurant.builder()
                .id(2L)
                .ruc("20756902102")
                .tradeName("Maria's Restaurant").build();
        category = CategoryDTO.builder()
                .name("Menus")
                .id(10L).build();
    }

    @Test
    @DisplayName("C. Get all restaurants in DB")
    void getAllRestaurants() throws Exception {
        // Given
        when(restaurantService.getAllRestaurants()).thenReturn(List.of(restaurant1, restaurant2));
        // When
        ResultActions response = mockMvc.perform(get("/"));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tradeName").value("Julio's Restaurant"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("C. Find restaurants by ID")
    void getRestaurantById() throws Exception {
        Long id = 1L;
        // Given
        when(restaurantService.findRestaurantById(id)).thenReturn(restaurant1);
        // When
        ResultActions response = mockMvc.perform(get("/findById/1"));
        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tradeName").value("Julio's Restaurant"))
                .andExpect(jsonPath("$.ruc").value("20756902101"))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("C. Save a restaurant in DB")
    void saveRestaurant() throws Exception {
        // Given
        when(restaurantService.saveRestaurant(restaurant1)).thenReturn(restaurant1);
        // When
        ResultActions resultActions = mockMvc.perform(
                post("/").content(objectMapper.writeValueAsString(restaurant1))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tradeName").value("Julio's Restaurant"))
                .andExpect(jsonPath("$.ruc").value("20756902101"));

    }

    @Test
    @DisplayName("C. Update a restaurant")
    void updateRestaurant() throws Exception {
        Long idUpdate = 1L;
        String newRuc = "10756393011";
        restaurant1.setRuc(newRuc);
        // Given
        when(restaurantService.updateRestaurant(idUpdate, restaurant1)).thenReturn(restaurant1);
        // When
        ResultActions resultActions = mockMvc.perform(
                put("/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurant1))
        );
        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ruc").value(newRuc))
                .andExpect(jsonPath("$.id").value(idUpdate));
    }

    @Test
    @DisplayName("C. Delete a restaurant")
    void deleteRestaurant() throws Exception{
        Long id = 1L;
        // Given
        doNothing().when(restaurantService).deleteRestaurantById(id);
        // When
        ResultActions resultActions = mockMvc.perform(delete("/1").contentType(MediaType.APPLICATION_JSON));
        // Then
        verify(restaurantService).deleteRestaurantById(id);
        resultActions.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("C. Get all categories by Restaurant ID")
    void getCategoriesByIdRestaurant() throws Exception{
        Long id = 1L;
        // Given
        when(restaurantService.getCategoriesById(id)).thenReturn(List.of(category));
        // When
        ResultActions resultActions = mockMvc.perform(get("/categories/1"));
        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Menus"));
    }
}