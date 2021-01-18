package com.microservices.auth.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserToSend {

    private Long userId;
    private String password;
    private String userName;
    private Long customerFK;
}
