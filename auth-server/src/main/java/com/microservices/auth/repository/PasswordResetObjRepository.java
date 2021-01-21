package com.microservices.auth.repository;

import com.microservices.auth.VO.PasswordResetObj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetObjRepository extends JpaRepository<PasswordResetObj, Long> {
    boolean existsByResetLink(String resetLink);
    Optional<PasswordResetObj> findByResetLink(String resetLink);
    void deleteByResetLink(String resetLink);
}
