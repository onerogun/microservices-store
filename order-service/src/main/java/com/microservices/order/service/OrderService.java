package com.microservices.order.service;

import com.microservices.order.converter.ConverterOrderContentOrderItem;
import com.microservices.order.entity.Order;
import com.microservices.order.entity.OrderItem;
import com.microservices.order.repository.OrderRepository;
import com.microservices.order.wrapper.OrderContentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.*;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service
@Slf4j
@EnableBinding(Source.class)
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ConverterOrderContentOrderItem converterOrderContentOrderItem;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Source source;

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

		AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.valueOf(0));

		orderItems.forEach(orderItem -> {
			total.updateAndGet(v ->
					v.add(orderItem.getOrderItemPrice()
							.multiply(BigDecimal.valueOf(orderItem.getOrderItemAmountOrdered()))));});
		//tax
		total.updateAndGet(bigDecimal -> bigDecimal.multiply(BigDecimal.valueOf(1.1)));
		log.info("Total: $$$" + total.toString());

		order = new Order();
		order.setOrderItems(orderItems);
		order.setCustomerId(userId);
		order.setOrderTime(LocalDateTime.now());
		order.setOrderTotal(total.get());

		updateAvailableStock(orders);

		Order savedOrder = orderRepository.save(order);


		/**
		 * Publish order to Message Broker(Rabbitmq) using Spring Cloud Stream and consume from email service
		 * RabbitMQ does NOT queue if consumer(Listener is not up) or add <requiredGroups> in properties file
		 */
		if (savedOrder != null) {
			publishEMail(savedOrder);
		}

		return   savedOrder;
		
	}


	private void publishEMail(Order orderPlaced) {
		source.output().send(MessageBuilder.withPayload(orderPlaced)
				.setHeader("order",orderPlaced.getOrderId()).build());
	}


	public List<Order> getCustomerOrders(Long customerId) {
		return orderRepository.findAllByCustomerIdOrderByOrderIdDesc(customerId);
	}

	private void updateAvailableStock(OrderContentList orders) {
		String updateStockUrl = "http://item-service/items/updateStock";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<OrderContentList> httpEntity = new HttpEntity<>(orders,headers);

		String response = restTemplate.postForObject(updateStockUrl, httpEntity, String.class);
		log.info(response);
	}

	public ResponseEntity<List<OrderItem>> saveCart(OrderContentList orderContentList, Long userId) {
		log.info("Inside of saveCart method of OrderService class, order-service");
		log.info(orderContentList.getOrderContentList().toString());
		List<OrderItem> orderItemList = new ArrayList<>();
		orderContentList.getOrderContentList().forEach(orderContent -> {
			orderItemList.add( converterOrderContentOrderItem.convert(orderContent));
		});

		return new ResponseEntity<>(orderItemList, HttpStatus.OK);
	}
}
