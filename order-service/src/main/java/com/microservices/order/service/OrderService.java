package com.microservices.order.service;

import com.microservices.order.converter.ConverterOrderContentOrderItem;
import com.microservices.order.entity.Order;
import com.microservices.order.entity.OrderItem;
import com.microservices.order.repository.OrderRepository;
import com.microservices.order.wrapper.OrderContentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ConverterOrderContentOrderItem converterOrderContentOrderItem;

	@Autowired
	private RestTemplate restTemplate;


	

	@Transactional
	public Order placeOrder(OrderContentList orders, Long userId) {
		log.info("Inside of placeOrder method of OrderService class, order-service");
		Order order;
		List<OrderItem> orderItems= new ArrayList<>();
		
		orders.getOrderContentList().forEach( orderContent -> 
		{
			orderItems.add(converterOrderContentOrderItem.convert(orderContent));
		}
				);
		
		order = new Order();
		order.setOrderItems(orderItems);
		order.setCustomerId(userId);
		order.setOrderTime(LocalDateTime.now());


		updateAvailableStock(orders);

		Order savedOrder = orderRepository.save(order);
		return   savedOrder;
		
	}


	public List<Order> getCustomerOrders(Long customerId) {
		return orderRepository.findAllByCustomerId(customerId);
	}

	private void updateAvailableStock(OrderContentList orders) {
		String updateStockUrl = "http://item-service/items/updateStock";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderContentList> httpEntity = new HttpEntity<>(orders,headers);

		String response = restTemplate.postForObject(updateStockUrl, httpEntity, String.class);
		log.info(response);
	}
}
