package com.microservices.email.service.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InputChannel {
        String ORDER_INPUT = "orderinput";
        String SIGN_IN_INPUT = "signininput";
        String PASSWORD_RESET_INPUT = "passwordresetinput";


        @Input("orderinput")
        SubscribableChannel inputOrder();

        @Input("signininput")
        SubscribableChannel inputSignIn();

        @Input("passwordresetinput")
        SubscribableChannel passwordResetInput();

}
