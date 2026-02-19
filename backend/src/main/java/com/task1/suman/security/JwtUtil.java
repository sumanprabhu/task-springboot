package com.task1.suman.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component  // So Spring can @Autowired this anywhere
public class JwtUtil {

    // This is our SECRET KEY — used to SIGN the token
    // Think of it as a STAMP that only our server knows
    // IMPORTANT: In real apps, put this in application.properties!
    private final String SECRET = "MyVerySecretKeyThatShouldBeLongEnoughForHS256Algorithm123!";

    // Token expires after 1 hour (in milliseconds)
    // 1000ms * 60s * 60min = 1 hour
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Convert our secret string into a proper cryptographic key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
        //     ↑↑↑↑
        //  Creates a secure key from our secret string
        //  HMAC = Hash-based Message Authentication Code
        //  It's the algorithm used to SIGN the token
    }

    // ============================================
    //  1. GENERATE TOKEN (called after login)
    // ============================================
    public String generateToken(String email, String role) {
        // This is like PRINTING a movie ticket

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        // claims = extra data we want inside the token

        return Jwts.builder()
                .claims(claims)                                          // extra data (role)
                .subject(email)                                          // WHO this token is for
                .issuedAt(new Date())                                    // WHEN was it created
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // WHEN does it expire
                .signWith(getSigningKey())                               // SIGN it with our secret
                .compact();                                              // Build the final token string
        //
        // What this creates:
        // Header:  {"alg": "HS256"}
        // Payload: {"sub": "suman@gmail.com", "role": "ADMIN", "iat": ..., "exp": ...}
        // Signature: HMAC-SHA256(header + payload, SECRET_KEY)
        //
        // Result: "eyJhbGci.eyJzdWI.signature"
    }

    // ============================================
    //  2. EXTRACT EMAIL from token
    // ============================================
    public String extractEmail(String token) {
        // Read the token and get the "subject" (email)
        return getClaims(token).getSubject();
    }

    // ============================================
    //  3. EXTRACT ROLE from token
    // ============================================
    public String extractRole(String token) {
        // Read the token and get the "role" we stored
        return (String) getClaims(token).get("role");
    }

    // ============================================
    //  4. CHECK if token is EXPIRED
    // ============================================
    public boolean isTokenExpired(String token) {
        // Is the expiration date BEFORE current time?
        return getClaims(token).getExpiration().before(new Date());
    }

    // ============================================
    //  5. VALIDATE token
    // ============================================
    public boolean validateToken(String token, String email) {
        // Token is valid IF:
        // 1. Email in token matches the email we expect
        // 2. Token is not expired
        String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    // ============================================
    //  HELPER: Read all claims from token
    // ============================================
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // Verify signature with our secret
                .build()
                .parseSignedClaims(token)      // Parse the token
                .getPayload();                 // Get the payload (claims)
        //
        // If token is TAMPERED or EXPIRED → this throws an exception!
        // It's like a security guard checking if the ticket is FAKE
    }
}