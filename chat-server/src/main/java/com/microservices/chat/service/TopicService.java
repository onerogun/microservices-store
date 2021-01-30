package com.microservices.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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
       if(topicMap.get(userId) != null) {
            if(!topicMap.get(userId).contains(topic)){
                topicMap.get(userId).add(topic);
                messagingTemplate.convertAndSend("/queue/" + userId, topicMap.get(userId));
            }
       } else {
           topicMap.put(userId, Arrays.asList(topic));
           messagingTemplate.convertAndSend("/queue/" + userId, topicMap.get(userId));
       }
    }
}
