package com.restaurants.microservice_restaurants.model.repository;

import com.restaurants.microservice_restaurants.model.entity.Logo;
import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantRepositoryTest {

    private Restaurant restaurant1, restaurant2;
    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.restaurant1 = Restaurant.builder()
                .id(1L)
                .ruc("20756902101")
                .tradeName("Julio's Restaurant").build();
        this.restaurant2 = Restaurant.builder()
                .id(2L)
                .ruc("20756902102")
                .tradeName("Maria's Restaurant").build();
    }

    @Test
    @DisplayName("R. Get all restaurants")
    public void getAllRestaurants() {
        // Given
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));
        // When
        List<Restaurant> restaurants = restaurantRepository.findAll();
        // Then
        assertThat(restaurants).isNotNull();
        assertThat(restaurants.size()).isEqualTo(2);
        assertThat(restaurants.getFirst()).isEqualTo(restaurant1);
    }

    @Test
    @DisplayName("R. Get a restaurant by Id")
    public void findRestaurantById() {
        Long id = 1L;
        Restaurant restaurant = null;
        // Given
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant1));
        // When
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(id);
        // Then
        assertThat(restaurantOpt).isNotNull();
        if (restaurantOpt.isPresent())
            restaurant = restaurantOpt.get();
        assert restaurant != null;
        assertThat(restaurant.getId()).isEqualTo(1L);
        assertThat(restaurant).isEqualTo(restaurant1);
    }

    @Test
    @DisplayName("R. Save restaurant test successfully")
    public void saveRestaurant() {
        // Given
        when(restaurantRepository.save(restaurant1)).thenReturn(restaurant1);
        // When
        Restaurant restaurantSaved = restaurantRepository.save(restaurant1);
        // Then
        assert restaurantSaved.getId() == 1L;
        assert Objects.equals(restaurantSaved.getRuc(), "20756902101");
        verify(restaurantRepository).save(restaurant1);
    }

    @Test
    @DisplayName("R. Update a restaurant correctly")
    public void updateRestaurantOk() {
        String newRuc = "10756902101";
        restaurant2.setRuc(newRuc);
        // Given
        when(restaurantRepository.save(restaurant2)).thenReturn(restaurant2);
        // When
        Restaurant restaurantUpdated = restaurantRepository.save(restaurant2);
        // Then
        assert restaurantUpdated.getId() == 2L;
        assert Objects.equals(restaurantUpdated.getRuc(), newRuc);
        assert Objects.equals(restaurantUpdated, restaurant2);
    }

    @Test
    @DisplayName("R. Delete a restaurant by it's id")
    public void deleteRestaurantById() {
        Long idToDelete = 2L;
        // Given
        doNothing().when(restaurantRepository).deleteById(idToDelete);
        // When
        restaurantRepository.deleteById(idToDelete);
        // Then
        verify(restaurantRepository).deleteById(idToDelete);
    }

}