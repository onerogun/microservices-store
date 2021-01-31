package com.microservices.auth.tokenrepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<TokenInDB, String> {
    boolean existsByToken(String token);
    void deleteByToken(String token);
}
