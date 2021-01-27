package com.microservices.cloud.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JAutoConfiguration;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
@EnableEurekaClient
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}

	@Bean
	public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(CircuitBreakerRegistry circuitBreakerRegistry, TimeLimiterRegistry timeLimiterRegistry) {
		TimeLimiterConfig timeLimiterConfig = timeLimiterRegistry.getDefaultConfig();
		ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
		reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
		reactiveResilience4JCircuitBreakerFactory.configure(resilience4JConfigBuilder -> resilience4JConfigBuilder.timeLimiterConfig(timeLimiterConfig), "backendA");
		return reactiveResilience4JCircuitBreakerFactory;
	}


}
