package com.microservices.order.controller;


import com.microservices.order.entity.Order;
import com.microservices.order.service.OrderService;
import com.microservices.order.wrapper.OrderContentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
	
	@Autowired
	private OrderService orderService;



	@PostMapping("/{userId}/placeOrder")
	public Order placeOrder(@RequestBody OrderContentList orders, @PathVariable Long userId) {
		log.info("Inside of placeOrder method of OrderController class, order-service");

		Order orderPlaced = orderService.placeOrder(orders, userId);
		return orderPlaced;
	}

	@GetMapping("/{customerId}/getOrderList")
	public  List<Order> getCustomerOrders(@PathVariable Long customerId) {
		log.info("Inside of getCustomerOrders method of OrderController class, order-service");

		return orderService.getCustomerOrders(customerId);
	}
}
