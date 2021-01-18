package com.microservices.email.service.services;

import com.microservices.email.service.VO.Customer;
import com.microservices.email.service.VO.Order;
import com.microservices.email.service.channel.InputChannel;
import com.microservices.email.service.config.EMailConfig;
import com.microservices.email.service.content.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@EnableBinding(InputChannel.class)
public class EMailService {

    @Autowired
    private EMailConfig eMailConfig;

    @Autowired
    private RestTemplate restTemplate;


    public void sendEmail(Content content) {
        JavaMailSenderImpl javaMailSender = getJavaMailSender();

        log.info(content.toString());
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(content.geteMail());
        simpleMailMessage.setTo("onerogun@gmail.com");
        simpleMailMessage.setSubject("New eMail from " + content.getUserName());
        simpleMailMessage.setText(content.getBody());

        javaMailSender.send(simpleMailMessage);

        sendNotification(content);
    }

    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        log.info(eMailConfig.toString());
        javaMailSender.setHost(eMailConfig.getHost());
        javaMailSender.setPort(eMailConfig.getPort());
        javaMailSender.setUsername(eMailConfig.getUserName());
        javaMailSender.setPassword(eMailConfig.getPassword());
        return javaMailSender;
    }

    private void sendNotification(Content content) {
        JavaMailSenderImpl javaMailSender = getJavaMailSender();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("noreply@store.com");
        simpleMailMessage.setTo(content.geteMail());
        simpleMailMessage.setSubject(content.getUserName() + " we received your message!");
        simpleMailMessage.setText(content.getUserName() + " we received your message and will resolve your issue asap!");

        javaMailSender.send(simpleMailMessage);
    }

    @StreamListener(InputChannel.ORDER_INPUT)
    private void consumeOrder(Order order) {
        log.info(order.toString());
        String orderPlacedSubject = " your order has been successfully placed!";
        eMailSender(order.getCustomerId(), orderPlacedSubject, order.toString());
    }

    @StreamListener(InputChannel.SIGN_IN_INPUT)
    private void consumeSignIn(Long customerFk) {
        log.info("Sending alert EMail to the customer with FK: " + customerFk);

        String signInAlertSubject = " Security Alert: SIGN IN";
        String signInAlertBody = " your account signed-in.";
        eMailSender(customerFk,signInAlertSubject, signInAlertBody );
    }

    private void eMailSender(Long customerId, String subject, String body) {
        JavaMailSenderImpl javaMailSender = getJavaMailSender();

        Customer customer = restTemplate.getForObject("http://customer-service/customer/"  + customerId, Customer.class);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("orderdetails@store.com");
        simpleMailMessage.setTo(customer.getCustomerEMail());
        simpleMailMessage.setSubject(customer.getCustomerName() + subject);
        simpleMailMessage.setText(customer.getCustomerName() + "\n" + body);

        javaMailSender.send(simpleMailMessage);
    }

}
