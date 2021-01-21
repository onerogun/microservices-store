package com.microservices.auth.service;

import com.microservices.auth.VO.Customer;
import com.microservices.auth.VO.CustomerSignup;
import com.microservices.auth.VO.PasswordResetObj;
import com.microservices.auth.VO.UserToSend;
import com.microservices.auth.applicationusers.User;
import com.microservices.auth.converter.UserToSendToUserConverter;
import com.microservices.auth.converter.UserToUserToSendConverter;
import com.microservices.auth.repository.PasswordResetObjRepository;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.security.PasswordConfig;
import com.microservices.auth.streamchannel.OutputChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@EnableBinding(OutputChannel.class)
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final RestTemplate restTemplate;
    private final UserToUserToSendConverter toSendConverter;
    private final UserToSendToUserConverter toUserConverter;
    private final OutputChannel outputChannel;
    private final PasswordResetObjRepository passwordResetObjRepository;

    public AuthService(UserRepository userRepository, PasswordConfig passwordConfig, RestTemplate restTemplate, UserToUserToSendConverter converter, UserToSendToUserConverter toUserConverter, OutputChannel outputChannel, PasswordResetObjRepository passwordResetObjRepository) {
        this.userRepository = userRepository;
        this.passwordConfig = passwordConfig;
        this.restTemplate = restTemplate;
        this.toSendConverter = converter;
        this.toUserConverter = toUserConverter;
        this.outputChannel = outputChannel;
        this.passwordResetObjRepository = passwordResetObjRepository;
    }

    public User save(User user) {
        log.info("In save method of AuthService class, auth-server");
        if(!userRepository.existsByUserName(user.getUserName())) {
            user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));

            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public List<User> getAll() {
        log.info("In getAll method of AuthService class, auth-server");
        return userRepository.findAll();
    }

    public User update(User user) {
        log.info("In update method of AuthService class, auth-server");
        if(userRepository.existsById(user.getUserId())) {
           return userRepository.save(user);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Long id) {
        log.info("In deleteUser method of AuthService class, auth-server");
        if(!userRepository.existsById(id)) {
            return false;
        } else {
            userRepository.deleteById(id);
            return true;
        }
    }

    public User getUser(Long id) {
        log.info("In getUser method of AuthService class, auth-server");
        return  userRepository.findById(id).orElse(null);
    }

    public UserToSend getUserToSend(Long id) {
        log.info("In getUserToSend method of AuthService class, auth-server");
       User user = userRepository.findById(id).orElse(null);
       return toSendConverter.convert(user);
    }

    public ResponseEntity<String> saveCustomer(CustomerSignup customerSignup) {
        log.info("In saveCustomer method of AuthService class, auth-server");
        boolean existByUserName = userRepository.existsByUserName(customerSignup.getUserName());
        boolean existByEMail = userRepository.existsByUserEMail(customerSignup.getCustomerEMail());
        if(!existByUserName && !existByEMail) {
            User user = new User();
            user.setUserName(customerSignup.getUserName());
            user.setPassword(passwordConfig.passwordEncoder().encode(customerSignup.getPassword()));
            user.setUserEMail(customerSignup.getCustomerEMail());
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

           return new ResponseEntity<>("Sign up successful!", HttpStatus.OK);
        }else if(existByUserName && existByEMail) {
            return new ResponseEntity<>("There is already an account with this User Name and e-mail, if your forgot your password, click Reset Password button!", HttpStatus.NOT_ACCEPTABLE);
        } else if(existByUserName) {
            return new ResponseEntity<>("Username exists, select another one!", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>("There is already an account with this e-mail, if your forgot your password, click Reset Password button!", HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public UserToSend UpdateReactUser(UserToSend userToUpdate) {
        log.info("In UpdateReactUser method of AuthService class, auth-server");
        if(userRepository.existsByUserName(userToUpdate.getUserName())) {
            return null;
        } else {
            User updated = userRepository.save(toUserConverter.convert(userToUpdate));
            UserToSend userUpdated = toSendConverter.convert(updated);
          return userUpdated;
        }
    }

    public void sendPasswordResetMail(String email) {
        log.info("In sendPasswordResetMail method of AuthService class, auth-server");
       if(userRepository.existsByUserEMail(email)) {
           String resetLink = String.valueOf(UUID.randomUUID());

           PasswordResetObj passwordResetObj = new PasswordResetObj();
           passwordResetObj.setEmail(email);
           passwordResetObj.setResetLink(resetLink);
           passwordResetObj.setLinkExpireDateTime(LocalDateTime.now().plusMinutes(30));

           PasswordResetObj saved = passwordResetObjRepository.save(passwordResetObj);

           outputChannel.passwordReset().send(MessageBuilder.withPayload(saved).build());
       }
    }



    @Transactional
    public ResponseEntity<String> resetPassword(String link, String password) {
        log.info("In resetPassword method of AuthService class, auth-server");
        if (passwordResetObjRepository.existsByResetLink(link)) {
            log.info("Link exists checking validity, auth-server");
            PasswordResetObj passwordResetObj = passwordResetObjRepository.findByResetLink(link).get();
            if(passwordResetObj.getLinkExpireDateTime().isAfter(LocalDateTime.now())) {

                log.info("Resetting password: " + password + "  \n" + passwordResetObj.toString());
                User userToResetPassword = userRepository.findByUserEMail(passwordResetObj.getEmail()).get();
                String encodedPassw = passwordConfig.passwordEncoder().encode(password);
                userToResetPassword.setPassword(encodedPassw);
                User updatedUser = userRepository.save(userToResetPassword);
                log.info("Updated passw: " + updatedUser.getPassword());
                log.info("saved passw: " + encodedPassw );
                if(!updatedUser.getPassword().equals(encodedPassw)) {
                    return new ResponseEntity<>("Update failed", HttpStatus.NOT_ACCEPTABLE);
                }
                passwordResetObjRepository.deleteByResetLink(link);
                return new ResponseEntity<>("Password has been successfully changed!", HttpStatus.ACCEPTED);
            } else {
                log.info("Link expired!");
                passwordResetObjRepository.deleteByResetLink(link);
                return new ResponseEntity<>("Link expired!", HttpStatus.BAD_REQUEST);
            }
        } else {
            log.info("Link does not exist!");
            return new ResponseEntity<>("Link does not exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Void> checkLinkValidity(String link) {
        log.info("In checkLinkValidity method of AuthService class, auth-server");
        if (passwordResetObjRepository.existsByResetLink(link)) {
            PasswordResetObj passwordResetObj = passwordResetObjRepository.findByResetLink(link).get();
            if(passwordResetObj.getLinkExpireDateTime().isAfter(LocalDateTime.now())) {
                log.info("Link valid!");
                return ResponseEntity.ok().build();
            } else {
                log.info("Link expired!");
                passwordResetObjRepository.deleteByResetLink(link);
                return ResponseEntity.badRequest().build();
            }
        } else {
            log.info("Link does not exist!");
            return ResponseEntity.badRequest().build();
        }
    }
}
