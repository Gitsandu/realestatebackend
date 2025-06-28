package com.example.RealEstateBackend.repository;

import com.example.RealEstateBackend.model.RefreshToken;
import com.example.RealEstateBackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUser(User user);
    
    void deleteByUser(User user);
    
    void deleteAllByExpiryDateBefore(Instant expiryDate);
}
