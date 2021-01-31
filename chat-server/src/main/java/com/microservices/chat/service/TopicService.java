package com.microservices.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TopicService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Map<Long, List<String>> topicMap;

    public TopicService() {
        this.topicMap = new HashMap<>();
    }

    public List<String> getTopics(Long userId) {
       return topicMap.get(userId);
    }

    /**
     * If there is a new topic, add it to list and send to subscriber with messaging template
     * If there is nothing new, don't do anything
     * @param userId
     * @param topic
     */
    public void addTopic(Long userId, String topic) {
        log.info("Inside of addTopic method, TopicService class, chat-server");
        log.info("user id: " + userId + " topic: " + topic);
       if(topicMap.get(userId) != null) {
           log.info("user has list");
            if(!topicMap.get(userId).contains(topic)){
                topicMap.get(userId).add(topic);
                messagingTemplate.convertAndSend("/queue/" + userId, topicMap.get(userId));
            }
       } else {
           List<String> topicList = new ArrayList<>();
           topicList.add(topic);
           topicMap.put(userId, topicList);
           messagingTemplate.convertAndSend("/queue/" + userId, topicMap.get(userId));
       }
    }
}
