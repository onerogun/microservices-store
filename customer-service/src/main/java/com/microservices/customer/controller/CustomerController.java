package com.microservices.customer.controller;

import com.microservices.customer.entity.Customer;
import com.microservices.customer.service.CustomerService;
import com.microservices.customer.wrapper.CustomerList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@GetMapping("/getall")
	public CustomerList getCustomers() {
		log.info("Inside of getCustomers method of CustomerController class, in customer-service");
		return customerService.getCustomerList();
	}

	@GetMapping("/{customerId}")
	public Customer getCustomer(@PathVariable Long customerId) {
		log.info("Inside of getCustomer method of CustomerController class, in customer-service");
		return customerService.getCustomer(customerId);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Customer> setCustomer(@RequestBody Customer customer) {
		log.info("Inside of setCustomer method of CustomerController class, in customer-service");
		return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		log.info("Inside of deleteCustomer method of CustomerController class, in customer-service");
		customerService.deleteCustomer(id);
	}
	
	@PutMapping("/update/{id}")
	public Customer updateCustomer(@RequestBody Customer customer, @PathVariable Long id) {
		log.info("Inside of updateCustomer method of CustomerController class, in customer-service");
		return customerService.saveCustomer(customer);
	}
}
