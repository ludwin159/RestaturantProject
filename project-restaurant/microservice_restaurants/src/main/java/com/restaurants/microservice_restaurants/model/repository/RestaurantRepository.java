package com.restaurants.microservice_restaurants.model.repository;

import com.restaurants.microservice_restaurants.model.entity.Restaurant;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
