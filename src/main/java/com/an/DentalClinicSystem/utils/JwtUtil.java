package com.an.DentalClinicSystem.utils;

import com.an.DentalClinicSystem.service.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

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
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
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
