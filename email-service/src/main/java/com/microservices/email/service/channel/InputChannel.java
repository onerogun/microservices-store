package com.microservices.email.service.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InputChannel {
        String ORDER_INPUT = "orderinput";
        String SIGN_IN_INPUT = "signininput";


        @Input("orderinput")
        SubscribableChannel inputOrder();

        @Input("signininput")
        SubscribableChannel inputSignIn();


}
