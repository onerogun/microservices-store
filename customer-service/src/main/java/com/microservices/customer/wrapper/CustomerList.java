package com.microservices.customer.wrapper;

import com.microservices.customer.entity.Customer;

import java.util.List;

public class CustomerList {

	private List<Customer> customerList;

	public CustomerList(List<Customer> customerList) {
		super();
		this.customerList = customerList;
	}

	public CustomerList() {
		super();
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}
	
	
}
