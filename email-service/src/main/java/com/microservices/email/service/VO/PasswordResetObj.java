package com.microservices.email.service.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PasswordResetObj {
    private Long id;

    private String email;
    private String resetLink;
}
