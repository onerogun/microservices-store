package com.microservices.email.service.controller;

import com.microservices.email.service.config.EMailConfig;
import com.microservices.email.service.content.Content;
import com.microservices.email.service.services.EMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EMailController {



    @Autowired
    private EMailService eMailService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody Content content, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return  ResponseEntity.unprocessableEntity().build();
        } else  {
            eMailService.sendEmail(content);
            return ResponseEntity.ok().build();
        }
    }
}
