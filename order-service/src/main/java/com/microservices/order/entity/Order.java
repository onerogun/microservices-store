package com.microservices.order.entity;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@DynamicUpdate
@Table(name="orders")	//Add table name to avoid wrong SQL hibernate join relation
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderId;

	private LocalDateTime orderTime;
	private Long customerId;


	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	@EqualsAndHashCode.Exclude		//to avoid LOMBOK errors
	private List<OrderItem> orderItems;

	public Order(Long orderId, LocalDateTime orderTime, Long customerId, List<OrderItem> orderItems) {
		this.orderId = orderId;
		this.orderTime = orderTime;
		this.customerId = customerId;
		this.orderItems = orderItems;
	}

	public Order() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}
