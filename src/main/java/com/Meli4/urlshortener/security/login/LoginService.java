package com.Meli4.urlshortener.security.login;

import com.Meli4.urlshortener.security.refresh.RefreshToken;
import com.Meli4.urlshortener.security.refresh.RefreshTokenRepository;
import com.Meli4.urlshortener.security.requests.LoginRequest;
import com.Meli4.urlshortener.security.requests.RegisterRequest;
import com.Meli4.urlshortener.security.response.JwtTokenResponse;
import com.Meli4.urlshortener.security.user.User;
import com.Meli4.urlshortener.security.user.UserService;
import com.Meli4.urlshortener.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class LoginService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Value("token.refresh.expiration")
    private long refreshExpiration; //jwtExpiration * 7

    public JwtTokenResponse register(RegisterRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.create(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration));
        refreshTokenRepository.save(refreshToken);

        return new JwtTokenResponse(jwtService.generateToken(user), refreshToken.getId());
    }

    public JwtTokenResponse login(LoginRequest request){
        String requestUsername = request.getUsername();
        String username = requestUsername;

        if(userService.getByEmail(requestUsername) != null){
            username = userService.getByEmail(requestUsername).getUsername();
        }

        if(userService.getByUsername(requestUsername) == null && userService.getByEmail(requestUsername) == null){
            throw new RuntimeException("No user found!");
        }

        UserDetails user = userService.getUserDetails().loadUserByUsername(username);
        User userEntity = userService.getByUsername(user.getUsername());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userEntity);
        refreshToken.setExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration));
        refreshTokenRepository.save(refreshToken);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.getPassword()));

        return new JwtTokenResponse(jwtService.generateToken(user), refreshToken.getId());
    }

    public JwtTokenResponse refreshToken(UUID refreshToken) {
        final var refreshTokenEntity = refreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshToken, new Date(System.currentTimeMillis()))
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));

        final var newAccessToken = jwtService
                .generateToken(refreshTokenEntity.getUser());
        return new JwtTokenResponse(newAccessToken, refreshToken);
    }
}
