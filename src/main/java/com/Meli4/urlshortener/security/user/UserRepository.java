package com.Meli4.urlshortener.security.user;

import com.Meli4.urlshortener.urlEntity.UrlEntityStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
