package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import com.example.demo.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdentifier(String identifier);
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByAccountTypeAndIdentifier(AccountType accountType, String identifier);
    boolean existsByIdentifier(String identifier);
}