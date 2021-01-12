package com.microservices.storage.controller;

import com.microservices.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/storage")
@Slf4j
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/save/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Path> saveFile(@PathVariable("productId") Long id, @RequestParam(name = "file") MultipartFile file) {
        log.info("Inside of saveFile method of StorageController class, storage-service");
        Path path = storageService.save(id, file);
        return new ResponseEntity<>(path, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getItemFiles/{itemId}")
    public ResponseEntity<byte[]> getItemFiles(@PathVariable Long itemId) {
        log.info("Inside of getItemFiles method of StorageController class, storage-service");
        return new ResponseEntity<>(storageService.getItemFiles(itemId), HttpStatus.OK);
    }

}
