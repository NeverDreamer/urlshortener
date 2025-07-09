package com.Meli4.urlshortener.security.response;

public class JwtTokenResponse {
    private String token;

    public JwtTokenResponse(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
