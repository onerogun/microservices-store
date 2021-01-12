package com.microservices.item.VO;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.nio.file.Path;


@Getter
@Setter
@ToString
@Document(collection = "Itemfilelocation")
public class PathObj {
    @Id
    private String id;
    private Long itemId;
    private String path;

    public PathObj(Long itemId, String path) {
        this.itemId = itemId;
        this.path = path;
    }
}
