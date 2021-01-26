package com.microservices.auth.VO;

import com.microservices.auth.applicationusers.UserRoles;
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
    private String userName;
    private String userEMail;
    private Long customerFK;
    private UserRoles userRoles;
}
