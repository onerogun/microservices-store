package com.microservices.chat.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


@RestController
public class VideoStreamController {


    @GetMapping(value = "/video")
    public ResponseEntity<InputStreamResource> stream() throws FileNotFoundException {

        File file = new File("F:\\Poco Media\\SendAnywhere\\976f3e6e-e3f3-408e-b899-8b6b47ee6abc.mp4");
        InputStream inputStream = new FileInputStream(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
        httpHeaders.set(HttpHeaders.CONTENT_RANGE, "50-1025/17839845");
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));


        return new ResponseEntity<>(new InputStreamResource(inputStream), httpHeaders, HttpStatus.OK);

    }


}