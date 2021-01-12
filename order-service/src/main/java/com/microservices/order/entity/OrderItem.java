package com.microservices.order.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@DynamicUpdate
@Table(name="orderitem")			//Add table name to avoid wrong SQL hibernate join relation
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderItemId;
	private int orderItemAmountOrdered;
	
	private Long orderItemItemId;
	private BigDecimal orderItemPrice;
	private String orderItemName;
	private String orderItemCategory;
	private boolean orderItemFeatured;

	@Column(name = "order_id")
	private Long orderId;

/* 	// Creates another table for relationship
	@ManyToOne
	@JoinColumn(name = "order_id")
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	private  Order order;
*/

	public OrderItem(Long orderItemId, int orderItemAmountOrdered, Long orderItemItemId, BigDecimal orderItemPrice, String orderItemName, String orderItemCategory, boolean orderItemFeatured, Long orderId) {
		this.orderItemId = orderItemId;
		this.orderItemAmountOrdered = orderItemAmountOrdered;
		this.orderItemItemId = orderItemItemId;
		this.orderItemPrice = orderItemPrice;
		this.orderItemName = orderItemName;
		this.orderItemCategory = orderItemCategory;
		this.orderItemFeatured = orderItemFeatured;
		this.orderId = orderId;
	}

	public OrderItem() {
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getOrderItemAmountOrdered() {
		return orderItemAmountOrdered;
	}

	public void setOrderItemAmountOrdered(int orderItemAmountOrdered) {
		this.orderItemAmountOrdered = orderItemAmountOrdered;
	}

	public Long getOrderItemItemId() {
		return orderItemItemId;
	}

	public void setOrderItemItemId(Long orderItemItemId) {
		this.orderItemItemId = orderItemItemId;
	}

	public BigDecimal getOrderItemPrice() {
		return orderItemPrice;
	}

	public void setOrderItemPrice(BigDecimal orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
	}

	public String getOrderItemName() {
		return orderItemName;
	}

	public void setOrderItemName(String orderItemName) {
		this.orderItemName = orderItemName;
	}

	public String getOrderItemCategory() {
		return orderItemCategory;
	}

	public void setOrderItemCategory(String orderItemCategory) {
		this.orderItemCategory = orderItemCategory;
	}

	public boolean isOrderItemFeatured() {
		return orderItemFeatured;
	}

	public void setOrderItemFeatured(boolean orderItemFeatured) {
		this.orderItemFeatured = orderItemFeatured;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
