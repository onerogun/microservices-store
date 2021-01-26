package com.microservices.auth.converter;

import com.microservices.auth.VO.UserToSend;
import com.microservices.auth.applicationusers.User;
import com.microservices.auth.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToSendToUserConverter implements Converter<UserToSend, User> {

    private final UserRepository userRepository;

    public UserToSendToUserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User convert(UserToSend userToSend) {
       User user = userRepository.findById(userToSend.getUserId()).get();
       user.setUserName(userToSend.getUserName());
       user.setUserEMail(user.getUserEMail());
       return user;
    }
}
