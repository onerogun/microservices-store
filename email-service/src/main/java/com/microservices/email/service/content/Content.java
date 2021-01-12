package com.microservices.email.service.content;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class Content {
    @NotNull
    private String userName;
    @NotNull
    @Email
    private String eMail;
    @NotNull
    @Min(20)
    private String body;

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
