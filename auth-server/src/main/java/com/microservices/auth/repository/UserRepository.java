package com.microservices.auth.repository;

import com.microservices.auth.applicationusers.User;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@DynamicUpdate
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);
    boolean existsByUserEMail(String userEMail);
    Optional<User> findByUserName(String userName);
}
