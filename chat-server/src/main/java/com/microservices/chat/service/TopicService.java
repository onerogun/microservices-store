package com.microservices.chat.service;

import com.microservices.chat.topicrepo.UserSubscribedTopicList;
import com.microservices.chat.topicrepo.UserSubscribedTopicListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

@Service
@Slf4j
public class TopicService {


    private final SimpMessageSendingOperations messagingTemplate;

    private final UserSubscribedTopicListRepository userSubscribedTopicListRepository;

    public TopicService(SimpMessageSendingOperations messagingTemplate, UserSubscribedTopicListRepository userSubscribedTopicListRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userSubscribedTopicListRepository = userSubscribedTopicListRepository;
    }
    

    public Set<String> getTopics(Long userId) {
        log.info("Inside of getTopics method, TopicService class, chat-server");
       UserSubscribedTopicList userSubscribedTopicList = userSubscribedTopicListRepository.findById(userId).block();
        return userSubscribedTopicList == null ? null : userSubscribedTopicList.getTopicList();
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
        userSubscribedTopicListRepository.existsById(userId).subscribe(aBoolean -> {
            if(aBoolean) {
                log.info("user has list");
                   userSubscribedTopicListRepository.findById(userId).subscribe(userSubscribedTopicList -> {
                       log.info("User list in DB: " + userSubscribedTopicList.toString());
                       userSubscribedTopicList.getTopicList().add(topic);
                       userSubscribedTopicListRepository.save(userSubscribedTopicList).subscribe(userSubscribedTopicList1 -> {
                           log.info("saved list in DB: " + userSubscribedTopicList1.toString());
                           messagingTemplate.convertAndSend("/queue/" + userId, userSubscribedTopicList1.getTopicList());
                       });
                   });


            } else {
                Set<String> topicList = new HashSet<>();
                topicList.add(topic);
               userSubscribedTopicListRepository.save(new UserSubscribedTopicList(userId, topicList)).subscribe(userSubscribedTopicList -> {
                   messagingTemplate.convertAndSend("/queue/" + userId, userSubscribedTopicList.getTopicList());
               });

            }
        });

    }
}
