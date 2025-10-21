package com.an.DentalClinicSystem.utils;

import com.an.DentalClinicSystem.service.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


@Slf4j
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${JWT_SECRET}")
    private String SECRET_KEY;

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public String generateToken(CustomUserDetails userDetails) {
        String rawRole = userDetails.getAuthorities().iterator().next().getAuthority(); // ví dụ: "ROLE_patient"
        String normalizedRole = rawRole.toUpperCase(); // thành "ROLE_PATIENT"

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", normalizedRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            log.warn("JWT expired at {}: {}", ex.getClaims().getExpiration(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("JWT error: {}", ex.getMessage());
            throw ex;
        }
    }

    public boolean validateToken(String token, CustomUserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }


}
