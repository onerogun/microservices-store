package com.microservices.auth.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {

	private Long customerId;
	private String customerName;
	private String customerEMail;
	private Long userFK;


}
