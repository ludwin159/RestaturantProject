package com.restaurants.microservice_restaurants.controller;

import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import com.restaurants.microservice_restaurants.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<?> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findRestaurantById(id));
    }

    @PostMapping
    public ResponseEntity<?> saveRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.saveRestaurant(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurant));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurantById(id);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoriesByIdRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getCategoriesById(id));
    }

}
