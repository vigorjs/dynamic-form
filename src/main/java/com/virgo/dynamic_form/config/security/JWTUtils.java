package com.virgo.dynamic_form.config.security;

import com.virgo.dynamic_form.model.enums.TokenType;
import com.virgo.dynamic_form.repository.TokenRepository;
import com.virgo.dynamic_form.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtils {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Value("${jwt.secret:c3VwZXJfc2VjcmV0X2tleV8yMDI0XzEyMzQ1Njc4OUFCQ0RE}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String generateToken(String email, TokenType tokenType) {
        try {
            long expiration = tokenType == TokenType.ACCESS ? accessTokenExpiration : refreshTokenExpiration;
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                    .claim("type", tokenType == TokenType.ACCESS ? "ACCESS" : "REFRESH")
                    .subject(email)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSecretKey())
                    .compact();
        } catch (Exception e) {
            log.error("Error generating token", e);
            throw new JwtException("Could not generate token");
        }
    }

    public boolean validateToken(String token) {
        try {
            //validasi signature
            Claims claims = extractClaims(token);
            if (claims == null) return false;

            // Validasi tipe token
            String tokenType = claims.get("type", String.class);
            if (tokenType == null) {
                log.warn("Token type is missing");
                return false;
            }

            // Validasi expiration
            if (claims.getExpiration() == null || claims.getExpiration().before(new Date())) {
                log.warn("Token is expired");
                return false;
            }

            // Validasi token di database
            return tokenRepository.findByToken(token)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

        } catch (Exception e) {
            log.error("Error validating token", e);
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            return extractClaims(token).getSubject();
        } catch (Exception e) {
            log.error("Error extracting email from token", e);
            throw new JwtException("Could not extract email from token");
        }
    }

    public TokenType getTokenType(String token){
        Claims claims = extractClaims(token);

        // Validasi tipe token
        String tokenType = claims.get("type", String.class);

        return TokenType.valueOf(tokenType);
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error extracting claims from token", e);
            return null;
        }
    }

    private SecretKey getSecretKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error generating secret key", e);
            throw new JwtException("Could not generate secret key");
        }
    }
}