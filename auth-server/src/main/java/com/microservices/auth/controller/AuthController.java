package com.microservices.auth.controller;

import com.microservices.auth.VO.CustomerSignup;
import com.microservices.auth.applicationusers.User;
import com.microservices.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        log.info("Inside of save method of AuthController of auth-server");
        User savedUser = authService.save(user);
        return new ResponseEntity<>(savedUser, savedUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<User> saveCustomer(@RequestBody CustomerSignup customerSignup) {
        log.info("Inside of saveCustomer method of AuthController of auth-server");
        log.info(customerSignup.toString());
        User savedUser = authService.saveCustomer(customerSignup);
        return new ResponseEntity<>(savedUser, savedUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        log.info("Inside of getAll method of AuthController of auth-server");
        return new ResponseEntity<>(authService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        log.info("Inside of updateUser method of AuthController of auth-server");
        User updatedUser = authService.update(user);
        return new ResponseEntity<>(updatedUser, updatedUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Inside of deleteUser method of AuthController of auth-server");
        return authService.deleteUser(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("Inside of getUser method of AuthController of auth-server");
        User user = authService.getUser(id);
        return new ResponseEntity<>(user, user == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }
}
