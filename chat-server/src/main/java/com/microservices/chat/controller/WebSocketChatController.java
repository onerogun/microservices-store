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

        try {

            if (message.getPublisher() == null || message.getSubscriber() == null) {
                throw new NullPointerException("Cannot be null!");
            }

            log.info(message.toString());
            String topicNameForSubscriber = generateTopicName(message.getPublisher(), message.getSubscriber());
            String topicNameForPublisher = generateTopicName(message.getSubscriber(), message.getPublisher());
            topicService.addTopic(Long.valueOf(message.getSubscriber()), topicNameForSubscriber);
            topicService.addTopic(Long.valueOf(message.getPublisher()), topicNameForPublisher);
            messagingTemplate.convertAndSend("/queue/" + topicNameForSubscriber, message.getContent());
            return message.getContent();

        } catch (NullPointerException ex) {
            log.info("Pub: " +message.getPublisher() + "Sub: " + message.getSubscriber());
        }
        return "Could not send message!";
    }


    private String generateTopicName(String pub, String sub) {
        return "Publisher:" + pub + "-Subscriber:" + sub;
    }


}

