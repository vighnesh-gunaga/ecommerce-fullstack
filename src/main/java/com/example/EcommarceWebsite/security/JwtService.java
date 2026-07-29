package com.example.EcommarceWebsite.security;

import com.example.EcommarceWebsite.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    private String secret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
    public String generateToken(User user)
    {


        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSecretKey())
                .compact();
    }
    public String extractEmailFromToken(String token)
    {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean isTokenValid(
            String token,
            User user
    ) {
        try {

            String email =
                    extractEmailFromToken(token);

            return email.equals(
                    user.getEmail()
            );

        } catch (Exception exception) {

            return false;
        }
    }
}
