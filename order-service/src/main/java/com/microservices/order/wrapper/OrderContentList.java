package com.microservices.order.wrapper;

import java.util.List;

public class OrderContentList {

	private List<OrderContent> orderContentList;

	public OrderContentList(List<OrderContent> orderContentList) {
		super();
		this.orderContentList = orderContentList;
	}

	public OrderContentList() {
		super();
	}

	public List<OrderContent> getOrderContentList() {
		return orderContentList;
	}

	public void setOrderContentList(List<OrderContent> orderContentList) {
		this.orderContentList = orderContentList;
	}
	
	
}
