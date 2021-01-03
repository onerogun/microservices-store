package com.microservices.customer.service;

import com.microservices.customer.entity.Customer;
import com.microservices.customer.repository.CustomerRepository;
import com.microservices.customer.wrapper.CustomerList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public CustomerList getCustomerList() {
		log.info("Inside of getCustomerList method of CustomerService class, in customer-service");
		CustomerList list = new CustomerList();
		
		list.setCustomerList(customerRepository.findAll());
		return list;
	}

	public Customer saveCustomer(Customer customer) {

		log.info("Inside of saveCustomer method of CustomerService class, in customer-service");
		return customerRepository.save(customer);
	}

	public void deleteCustomer(Long id) {

		log.info("Inside of deleteCustomer method of CustomerService class, in customer-service");
		customerRepository.deleteById(id);
	}


	public Customer getCustomer(Long customerId) {
		log.info("Inside of deleteCustomer method of CustomerService class, in customer-service");
		return customerRepository.findById(customerId).get();
	}
}
