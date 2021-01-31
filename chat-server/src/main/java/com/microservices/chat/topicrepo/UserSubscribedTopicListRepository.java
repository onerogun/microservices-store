package com.microservices.chat.topicrepo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserSubscribedTopicListRepository extends ReactiveMongoRepository<UserSubscribedTopicList, Long> {
}
