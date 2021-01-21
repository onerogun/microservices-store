package com.microservices.order.wrapper;

import lombok.ToString;

@ToString
public class OrderContent {
	


	private Long itemId;
	private int itemAmount;
	
	
	public OrderContent(Long itemId, int itemAmount) {
		this.itemId = itemId;
		this.itemAmount = itemAmount;
	}
	
	public OrderContent() {

	}
		
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public int getItemAmount() {
		return itemAmount;
	}
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}
	
	

	
	
	
	
}
