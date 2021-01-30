package com.microservices.chat.controller;

import com.microservices.chat.message.WebSocketChatMessage;
import com.microservices.chat.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final TopicService topicService;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, TopicService topicService) {
        this.messagingTemplate = messagingTemplate;
        this.topicService = topicService;
    }

    /**
     *  Destination from front end "/app/chat" comes here and sent back to publisher after
     *  being sent to subscriber first with messaging template.
     *  Also subscriber first needs to know where to subscribe so each user will have a list of open topics
     *  and he will subscribe to all of them.When a new topic opened, it is added to subscriber's specific topic list
     *  and then list sent to subscriber to subscribe all topics from front end stomp websocket
     * @param message
     * @return
     */

    @MessageMapping("/chat")
    @SendToUser
    public String chat(@Payload WebSocketChatMessage message) {

            log.info(message.toString());
            String topicName = generateTopicName(message.getPublisher(), message.getSubscriber());
            topicService.addTopic(Long.valueOf(message.getSubscriber()),topicName );
            messagingTemplate.convertAndSend("/queue/" + topicName, message.getContent());
            return message.getContent();

    }


    private String generateTopicName(String pub, String sub) {
        return "Publisher:" + pub + "-Subscriber:" + sub;
    }


}

