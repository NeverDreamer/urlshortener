package com.Meli4.urlshortener.util;

import com.Meli4.urlshortener.security.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {
    private static String secretKey = "+FUIncwvaKMAfuQGFdy/o9TQUO7rtmPxNWcFa/SOsrk=";
    private static long jwtExpiration = 144000000; //100000 * 60 * 24

    public static String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        if(userDetails instanceof User user){
            claims.put("id", user.getId());
            claims.put("email", user.getEmail());
            claims.put("username", user.getUsername());
        }

        return generateToken(claims, userDetails);
    }

    public static String generateToken(Map<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                    .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public static boolean isTokenValid(String token, UserDetails userDetails){
        String username = userDetails.getUsername();
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public static boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    public static String extractUsername(String token){
        return getClaim(token, Claims::getSubject);
    }

    private static <T> T getClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    private static Claims getAllClaims(String token){
        return Jwts.parser()
                .decryptWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
