package com.Meli4.urlshortener.security.login;

import com.Meli4.urlshortener.security.requests.LoginRequest;
import com.Meli4.urlshortener.security.requests.RegisterRequest;
import com.Meli4.urlshortener.security.response.JwtTokenResponse;
import com.Meli4.urlshortener.security.user.User;
import com.Meli4.urlshortener.security.user.UserService;
import com.Meli4.urlshortener.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    public JwtTokenResponse register(RegisterRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.create(user);

        return new JwtTokenResponse(JwtUtil.generateToken(user));
    }

    public JwtTokenResponse login(LoginRequest request){
        String requestUsername = request.getUsername();
        String username = requestUsername;

        if(userService.getByEmail(requestUsername) != null){
            username = userService.getByEmail(requestUsername).getUsername();
        }

        if(userService.getByUsername(requestUsername) == null || userService.getByEmail(requestUsername) == null){
            throw new RuntimeException("No user found!");
        }

        UserDetails user = userService.getUserDetails().loadUserByUsername(username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.getPassword()));

        return new JwtTokenResponse(JwtUtil.generateToken(user));
    }
}
