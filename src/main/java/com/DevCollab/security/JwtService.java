package com.DevCollab.security;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {


    private final String SECRET_KEY =
            "DevCollabSecretKey2026ForJwtAuthentication";


    private final long EXPIRATION_TIME =
            1000 * 60 * 60;


    // =====================================================
    // GENERATE TOKEN
    // =====================================================

    public String generateToken(
            Long userId,
            String email,
            List<String> roles) {


        return Jwts.builder()

                .setSubject(email)

                .claim(
                        "userId",
                        userId
                )

                .claim(
                        "roles",
                        roles
                )

                .setIssuedAt(
                        new Date()
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + EXPIRATION_TIME
                        )
                )

                .signWith(
                        SignatureAlgorithm.HS256,
                        SECRET_KEY
                )

                .compact();
    }


    // =====================================================
    // EXTRACT EMAIL
    // =====================================================

    public String extractEmail(
            String token) {

        return getClaims(token)
                .getSubject();
    }


    // =====================================================
    // EXTRACT USER ID
    // =====================================================

    public Long extractUserId(
            String token) {

        return getClaims(token)
                .get("userId", Long.class);
    }


    // =====================================================
    // EXTRACT ROLES
    // =====================================================

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(
            String token) {

        return getClaims(token)
                .get("roles", List.class);
    }


    // =====================================================
    // VALIDATE TOKEN
    // =====================================================

    public boolean validateToken(
            String token) {

        try {

            getClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }


    // =====================================================
    // GET CLAIMS
    // =====================================================

    private Claims getClaims(
            String token) {

        return Jwts.parser()

                .setSigningKey(
                        SECRET_KEY
                )

                .parseClaimsJws(token)

                .getBody();
    }
}