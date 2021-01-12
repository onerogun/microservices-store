package com.microservices.storage.service;

import com.google.common.io.ByteStreams;
import com.microservices.storage.VO.PathObj;
import com.microservices.storage.VO.PathObjList;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
@EnableBinding(Source.class)
public class StorageService {

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;

    private final String FILE_PATH = "pics";

    @Autowired
    private Source source;

    @Autowired
    private RestTemplate restTemplate;

    public Path save(Long id, MultipartFile file) {
        log.info("Inside of save method of StorageService class, storage-service");
        log.info(uploadDir.toString());


        Path location = Paths.get(uploadDir + File.separator + FILE_PATH + File.separator + StringUtils.cleanPath(String.valueOf(id)));

        File directory = new File(String.valueOf(location));
        if(!directory.exists()) {
            directory.mkdir();
        }
        location = Paths.get(location + File.separator  + StringUtils.cleanPath(UUID.randomUUID() +file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("File stored: " + location.toAbsolutePath());

        PathObj pathObj = new PathObj();
        pathObj.setPath(location);
        pathObj.setItemId(id);
        publishPath(pathObj);
        return location;
    }

    /**
     * Send path info to rabbitMQ and get from item service to save to database
     */
    private void publishPath(PathObj pathObj) {
        log.info("Inside of publishPath method of StorageService class, storage-service");
        source.output().send(MessageBuilder.withPayload(pathObj).setHeader("itemId", pathObj.getItemId()).build());
    }

    public byte[] getItemFiles(Long itemId) {
        log.info("Inside of getItemFiles method of StorageService class, storage-service");
        PathObjList response= restTemplate.getForObject("http://item-service/items/getItemFileLocations/" + itemId, PathObjList.class);


        Iterator<PathObj> iterator = response.getPathObjList().iterator();

        while (iterator.hasNext()) {
                    Path path = iterator.next().getPath();

                log.info(path.toAbsolutePath().toString());
                if (Files.exists(path)) {
                    try {

                        DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(path));
                        int size = dataInputStream.available(); //size of the Input stream
                        byte[] data = new byte[size];
                        dataInputStream.read(data);
                      //  byte[] data = ByteStreams.toByteArray(Files.newInputStream(path));
                        return data;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

        }
        return null;
    }
}
