package com.microservices.auth.controller;

import com.microservices.auth.VO.CustomerSignup;
import com.microservices.auth.VO.UserToSend;
import com.microservices.auth.applicationusers.User;
import com.microservices.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        authService.sendPasswordResetMail(email);
        String returnMessage = "We will send you a password reset e-mail if you have an account with submitted e-mail";
        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }

    @GetMapping("/checkJWT")
    public ResponseEntity<Void> checkJWT() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        log.info("Inside of save method of AuthController of auth-server");
        User savedUser = authService.save(user);
        return new ResponseEntity<>(savedUser, savedUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<String> saveCustomer(@RequestBody CustomerSignup customerSignup) {
        log.info("Inside of saveCustomer method of AuthController of auth-server");
        log.info(customerSignup.toString());
        ResponseEntity<String> responseEntity = authService.saveCustomer(customerSignup);
        return responseEntity;
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
    public ResponseEntity<UserToSend> getUserToSend(@PathVariable Long id) {
        log.info("Inside of getUser method of AuthController of auth-server");
        UserToSend userToSend = authService.getUserToSend(id);
        return new ResponseEntity<>(userToSend, userToSend == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PostMapping("/updateUserReact")
    public ResponseEntity<UserToSend> updateUserReact(@RequestBody UserToSend userToSend) {
        log.info("Inside of getUser method of AuthController of auth-server");
        UserToSend userUpdated = authService.UpdateReactUser(userToSend);
        return new ResponseEntity<>(userUpdated, userUpdated == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PostMapping("/resetPasswordByLink/{link}")
    public ResponseEntity<String> passwordResetRequest(@PathVariable String link, @RequestBody String password) {
        log.info("Inside of resetPassword method of AuthController of auth-server");
        return authService.resetPassword(link, password);
    }

    @GetMapping("/passwordReset/checkLinkValidity/{link}")
    public ResponseEntity<Void> checkLinkValidity(@PathVariable String link) {
        log.info("Inside of checkLinkValidity method of AuthController of auth-server");
        return authService.checkLinkValidity(link);
    }
}
