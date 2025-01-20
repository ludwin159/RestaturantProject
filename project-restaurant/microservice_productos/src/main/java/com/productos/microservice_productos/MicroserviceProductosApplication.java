package com.productos.microservice_productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProductosApplication.class, args);
	}

}
