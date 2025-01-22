package com.restaurants.microservice_restaurants.service;

import com.commons.CategoryDTO;
import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
    public List<Restaurant> getAllRestaurants();
    public Restaurant findRestaurantById(Long idRestaurant);
    public Restaurant saveRestaurant(Restaurant restaurant);
    public Restaurant updateRestaurant(Long id, Restaurant newRestaurant);
    public void deleteRestaurantById(Long id);
    public List<CategoryDTO> getCategoriesById(Long idRestaurant);
}
