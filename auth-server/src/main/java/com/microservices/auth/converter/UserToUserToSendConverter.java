package com.microservices.auth.converter;

import com.microservices.auth.VO.UserToSend;
import com.microservices.auth.applicationusers.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserToSendConverter implements Converter<User, UserToSend> {
    @Override
    public UserToSend convert(User user) {
        if(user == null) {
            return null;
        }

        UserToSend userToSend = new UserToSend();
        userToSend.setUserId(user.getUserId());
        userToSend.setUserName(user.getUserName());
        userToSend.setCustomerFK(user.getCustomerFK());
        userToSend.setUserEMail(user.getUserEMail());
        userToSend.setUserRoles(user.getRoles());
        return userToSend;
    }
}
