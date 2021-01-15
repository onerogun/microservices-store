package com.microservices.itemdetailsreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ItemDetailsReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemDetailsReactiveApplication.class, args);
	}

}
