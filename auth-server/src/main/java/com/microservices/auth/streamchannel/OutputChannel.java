package com.microservices.auth.streamchannel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutputChannel {

        String SIGN_IN_ALERT = "signinalert";
        String PASSWORD_RESET = "passwordreset";

        @Output("signinalert")
        MessageChannel signInAlert();

        @Output("passwordreset")
        MessageChannel passwordReset();

}
