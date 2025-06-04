//package com.example.Login.service;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class JwtService {
//    private static final String SECRET_KEY = "MI_CLAVE_SECRETA_DE_32_CARACTERES_O_MAS";
//
//    public String generateToken(String email, int rol) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("rol", rol)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira en 1 hora
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // Se usa getBytes() para compatibilidad con jjwt 0.9.1
//                .compact();
//    }
//
//    public String extractEmail(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY.getBytes()) // Se usa getBytes() en vez de una clave con Keys.hmacShaKeyFor()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//}

package com.example.Login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String SECRET_KEY = "MI_CLAVE_SECRETA_DE_32_CARACTERES_O_MAS";

    public String generateToken(String email, int rol, int id, String nombre) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)
                .claim("id", id)
                .claim("nombreUsuario", nombre)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Integer extractRol(String token) {
        return (Integer) extractAllClaims(token).get("rol");
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        Integer rol = extractRol(token);

        // Mapeás tu rol numérico a una autoridad; podés adaptarlo según tu app
        String roleName = switch (rol) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_USER";
            default -> "ROLE_INVITADO";
        };

        return List.of(new SimpleGrantedAuthority(roleName));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
