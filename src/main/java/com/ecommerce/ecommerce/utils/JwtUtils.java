package com.ecommerce.ecommerce.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    private final SecretKey secretKey;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    // Retrieve username from jwt token
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve role from jwt token
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }
    public String getNameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("name", String.class));
    }

    // Retrieve expiration date from jwt token
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // For retrieving any information from token, use the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    // Check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate token for user
    public String generateToken(String name, String role, String email, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("name", name);
        claims.put("userId", userId);
        return doGenerateToken(claims,email);
    }

    // Generate a JWT token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }
}
