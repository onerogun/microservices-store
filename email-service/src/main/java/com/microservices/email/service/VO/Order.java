package com.microservices.email.service.VO;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Data
@ToString
public class Order {
	

	private Long orderId;

	private LocalDateTime orderTime;
	private Long customerId;

	private List<OrderItem> orderItems;

}
