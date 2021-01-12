package com.microservices.auth.repository;

import com.microservices.auth.applicationusers.ApplicationUserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ApplicationUserDao {

    Optional<ApplicationUserDetails> selectApplicationUserByUsername(String username);

}
