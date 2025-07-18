package com.Meli4.urlshortener.security.login;

import com.Meli4.urlshortener.security.requests.LoginRequest;
import com.Meli4.urlshortener.security.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request));
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(loginService.register(request));
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<?> refresh(@RequestParam UUID refreshToken) {
        return ResponseEntity.ok(loginService.refreshToken(refreshToken));
    }
}
