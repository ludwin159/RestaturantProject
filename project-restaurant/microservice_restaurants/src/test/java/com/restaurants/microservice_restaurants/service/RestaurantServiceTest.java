package com.restaurants.microservice_restaurants.service;

import com.commons.CategoryDTO;
import com.restaurants.microservice_restaurants.exception.RestaurantNotFoundException;
import com.restaurants.microservice_restaurants.feignClient.CategoryClient;
import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import com.restaurants.microservice_restaurants.model.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {
    private Restaurant restaurant1, restaurant2;
    private CategoryDTO category;
    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CategoryClient categoryClient;

    @InjectMocks
    private RestaurantServiceImp restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.restaurant1 = Restaurant.builder()
                .id(1L)
                .ruc("20756902101")
                .tradeName("Julio's Restaurant").build();
        this.restaurant2 = Restaurant.builder()
                .id(2L)
                .ruc("20756902102")
                .tradeName("Maria's Restaurant").build();
        category = CategoryDTO.builder()
                .name("Menus")
                .id(10L).build();
    }

    @Test
    @DisplayName("S. Get all restaurants in db")
    void getAllRestaurants() {
        // Given
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));
        // When
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        // Then
        assert restaurants.size() == 2;
        assert restaurants.getFirst() == restaurant1;
        assert restaurants.getFirst().getId() == 1L;
        assert restaurants.getLast() == restaurant2;
    }

    @Test
    @DisplayName("S. Find a restaurant by it's id.")
    void findRestaurantById() {
        Long idToFind = 1L;
        // Given
        when(restaurantRepository.findById(idToFind)).thenReturn(Optional.of(restaurant1));
        // When
        Restaurant restaurantFound = restaurantService.findRestaurantById(idToFind);
        // Then
        assert restaurantFound.equals(restaurant1);
        assert Objects.equals(restaurantFound.getId(), 1L);
        assert Objects.equals(restaurantFound.getRuc(), "20756902101");

    }

    @Test
    @DisplayName("S. Find a restaurant when that not exists!")
    void findRestaurantByIdButNotFound() {
        Long idToFind = 3L;
        // Given
        when(restaurantRepository.findById(idToFind)).thenReturn(Optional.empty());
        // When
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () -> restaurantService.findRestaurantById(idToFind));
        // Then
        assert Objects.equals(exception.getMessage(), "Restaurant not found!");
        verify(restaurantRepository).findById(idToFind);
    }

    @Test
    @DisplayName("S. Save a restaurant correctly")
    void saveRestaurant() {
        // Given
        when(restaurantRepository.save(restaurant1)).thenReturn(restaurant1);
        // When
        Restaurant restaurant = restaurantService.saveRestaurant(restaurant1);
        // Then
        assert Objects.equals(restaurant.getId(), 1L);
        assert restaurant.getTradeName().contains("Julio");
        assert restaurant.equals(restaurant1);
    }

    @Test
    @DisplayName("S. Update a restaurant by it's Id")
    void updateRestaurant() {
        Long idUpdate = 2L;
        String newRuc = "10756902101";
        // Given
        restaurant2.setRuc(newRuc);
        when(restaurantRepository.save(restaurant2)).thenReturn(restaurant2);
        when(restaurantRepository.findById(idUpdate)).thenReturn(Optional.of(restaurant2));
        // When
        Restaurant restaurantUpdated = restaurantService.updateRestaurant(idUpdate, restaurant2);
        // Then
        assert Objects.equals(restaurantUpdated.getId(), idUpdate);
        assert Objects.equals(restaurantUpdated.getRuc(), newRuc);
        assert restaurantUpdated.equals(restaurant2);
    }

    @Test
    @DisplayName("S. Delete restaurant by it's ID")
    void deleteRestaurantById() {
        Long idToDelete = 1L;
        // Given
        doNothing().when(restaurantRepository).deleteById(idToDelete);
        when(restaurantRepository.findById(idToDelete)).thenReturn(Optional.of(restaurant1));
        // When
        restaurantService.deleteRestaurantById(idToDelete);
        // Then
        verify(restaurantRepository).deleteById(idToDelete);
    }

    @Test
    @DisplayName("S. Feign Client get categories by Restaurant ID")
    void getCategoriesById() {
        Long restaurantId = 1L;
        // Given
        when(categoryClient.getCategoriesByRestaurantId(restaurantId)).thenReturn(List.of(category));
        // When
        List<CategoryDTO> categoriesByRestaurant = restaurantService.getCategoriesById(restaurantId);
        // Then
        assert categoriesByRestaurant.size() == 1;
        assert categoriesByRestaurant.getFirst().getId().equals(10L);
        assert categoriesByRestaurant.getFirst().getName().equalsIgnoreCase("MENUS");
    }
}