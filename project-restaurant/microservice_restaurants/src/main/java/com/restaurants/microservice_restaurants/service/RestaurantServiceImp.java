package com.restaurants.microservice_restaurants.service;

import com.commons.CategoryDTO;
import com.restaurants.microservice_restaurants.exception.RestaurantNotFoundException;
import com.restaurants.microservice_restaurants.feignClient.CategoryClient;
import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import com.restaurants.microservice_restaurants.model.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RestaurantServiceImp implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryClient categoryClient;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant findRestaurantById(Long idRestaurant) {
        return restaurantRepository.findById(idRestaurant).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant not found!"));
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant newRestaurant) {
        return restaurantRepository.findById(id)
                .map(oldRestaurant -> {
                    oldRestaurant.setLogo(newRestaurant.getLogo());
                    oldRestaurant.setRuc(newRestaurant.getRuc());
                    oldRestaurant.setTradeName(newRestaurant.getTradeName());
                    oldRestaurant.setTableNumber(newRestaurant.getTableNumber());
                    restaurantRepository.save(oldRestaurant);
                    return oldRestaurant;
                })
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found!"));
    }

    @Override
    public void deleteRestaurantById(Long id) {
        restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurantRepository.deleteById(id);
                    return restaurant;
                })
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found!"));
    }

    @Override
    public List<CategoryDTO> getCategoriesById(Long idRestaurant) {
        return categoryClient.getCategoriesByRestaurantId(idRestaurant);
    }
}
