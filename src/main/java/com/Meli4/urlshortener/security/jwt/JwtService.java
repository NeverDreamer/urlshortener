package com.Meli4.urlshortener.security.jwt;

import com.Meli4.urlshortener.security.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String secretKey;
    @Value("${token.jwt.expiration}")
    private long jwtExpiration; //100000 * 60 * 24

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        if(userDetails instanceof User user){
            claims.put("id", user.getId());
            claims.put("email", user.getEmail());
            claims.put("username", user.getUsername());
        }

        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                    .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = userDetails.getUsername();
        return getAllClaims(token).getSubject().equals(username) &&
                username.equals(extractUsername(token)) &&
                !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
