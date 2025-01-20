package com.restaurants.microservice_restaurants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceRestaurantsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceRestaurantsApplication.class, args);
	}

}
