package com.microservices.chat.topicrepo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(collection = "UserSubscribedTopicList")
public class UserSubscribedTopicList {
    @Id
    private Long userId;
    private Set<String> topicList;
}
