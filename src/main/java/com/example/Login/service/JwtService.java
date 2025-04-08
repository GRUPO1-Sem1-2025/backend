package com.example.Login.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "MI_CLAVE_SECRETA_DE_32_CARACTERES_O_MAS";

    public String generateToken(String email, int rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira en 1 hora
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Se usa getBytes() para compatibilidad con jjwt 0.9.1
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // Se usa getBytes() en vez de una clave con Keys.hmacShaKeyFor()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
