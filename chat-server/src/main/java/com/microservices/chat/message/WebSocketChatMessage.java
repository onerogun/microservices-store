package com.microservices.chat.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class WebSocketChatMessage {

    private String content;
    private String publisher;
    private String subscriber;
}
