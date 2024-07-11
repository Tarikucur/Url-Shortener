package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {
        Optional<UserEntity> userOptional = repository.findByIdentifier(username);
        UserEntity user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with: " + username));
        return new User(
                user.getIdentifier(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public UserDetails loadUserById(String id) {
        UserEntity user = repository.findById(id).orElse(null);
        return new User(
                user.getIdentifier(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    public UserEntity findById(String userId)
    {
        return  repository.findById(userId).orElse(null);
    }
}