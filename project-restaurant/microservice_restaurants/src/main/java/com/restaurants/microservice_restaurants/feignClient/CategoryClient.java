package com.restaurants.microservice_restaurants.feignClient;

import com.commons.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "microservice-productos")
public interface CategoryClient {
    @GetMapping("/categoriesByRestaurant/{idRestaurant}")
    public List<CategoryDTO> getCategoriesByRestaurantId(@PathVariable Long id);
}
