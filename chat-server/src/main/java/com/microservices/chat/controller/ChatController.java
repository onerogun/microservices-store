package com.microservices.chat.controller;

import com.microservices.chat.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@Slf4j
public class ChatController {

    private final TopicService topicService;

    public ChatController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * When a subscriber opens a websocket, previously subscribed list of topics are sent
     * so that he can subscribe again
     * @param userId
     * @return
     */

    @SubscribeMapping("/getTopics/{userId}")
    public Set<String> getSubscribedTopics(@DestinationVariable Long userId) {
        log.info("<<<<<<<<<<<<<< sending topics:: >>>>>>>>> " + userId);
       return topicService.getTopics(userId);
    }


}
