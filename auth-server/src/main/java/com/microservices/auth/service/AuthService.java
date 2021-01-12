package com.microservices.auth.service;

import com.microservices.auth.VO.Customer;
import com.microservices.auth.VO.CustomerSignup;
import com.microservices.auth.applicationusers.User;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.security.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordConfig passwordConfig, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordConfig = passwordConfig;
        this.restTemplate = restTemplate;
    }

    public User save(User user) {
        if(!userRepository.existsByUserName(user.getUserName())) {
            user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));

            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(User user) {
        if(userRepository.existsById(user.getUserId())) {
           return userRepository.save(user);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            return false;
        } else {
            userRepository.deleteById(id);
            return true;
        }
    }

    public User getUser(Long id) {
        return  userRepository.findById(id).orElse(null);
    }

    public User saveCustomer(CustomerSignup customerSignup) {
        if(!userRepository.existsByUserName(customerSignup.getUserName())) {
            User user = new User();
            user.setUserName(customerSignup.getUserName());
            user.setPassword(passwordConfig.passwordEncoder().encode(customerSignup.getPassword()));
            user.setRoles(customerSignup.getRoles());
            user.setAccountNonExpired(customerSignup.isAccountNonExpired());
            user.setAccountNonLocked(customerSignup.isAccountNonLocked());
            user.setCredentialsNonExpired(customerSignup.isCredentialsNonExpired());
            user.setEnabled(customerSignup.isEnabled());

            /**
             * Save User first and get primary key to add in Customer
             */
            User savedUser = userRepository.save(user);

            Customer customer = new Customer();
            customer.setCustomerName(customerSignup.getCustomerName());
            customer.setCustomerEMail(customerSignup.getCustomerEMail());
            customer.setUserFK(savedUser.getUserId());

            String url = "http://customer-service/customer/save";


            /**
             * Add customer and save its primary key as foreign key
             */
            ResponseEntity<Customer> responseEntity = restTemplate.postForEntity(url, customer, Customer.class);

            savedUser.setCustomerFK(responseEntity.getBody().getCustomerId());
            User updatedUser = userRepository.save(savedUser);

            return updatedUser;
        }else {
            return null;
        }
    }
}
