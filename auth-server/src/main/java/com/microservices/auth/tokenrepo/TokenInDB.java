package com.microservices.auth.tokenrepo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "Token")
public class TokenInDB {
    @Id
    private String userName;
    private String token;

    public TokenInDB(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }
}
