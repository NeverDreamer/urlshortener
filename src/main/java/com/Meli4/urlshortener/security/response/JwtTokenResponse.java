package com.Meli4.urlshortener.security.response;

import java.util.UUID;

public class JwtTokenResponse {
    private String token;
    private UUID refreshToken;

    public JwtTokenResponse(String token, UUID refreshToken){
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }
}
