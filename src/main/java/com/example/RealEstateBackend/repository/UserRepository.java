package com.example.RealEstateBackend.repository;

import com.example.RealEstateBackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("{'enabled': true}")
    Page<User> findAllActiveUsers(Pageable pageable);
    
    @Query("{'enabled': ?0}")
    Page<User> findByStatus(boolean status, Pageable pageable);
    
    @Query("{'role': ?0}")
    List<User> findByRole(User.UserRole role);
    
    @Query("{'email': {$regex: ?0, $options: 'i'}}")
    List<User> findByEmailContaining(String email);
}
