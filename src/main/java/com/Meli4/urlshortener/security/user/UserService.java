package com.Meli4.urlshortener.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    @Transactional
    public User create(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("User with such username already exists");
        }else if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("User with such email already exists");
        }

        return save(user);
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserDetailsService getUserDetails(){
        return this::getByUsername;
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return getByUsername(username);
    }
}
