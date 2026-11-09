package org.example.gastos.infraestructure.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Service
public class JWTService {

    private final SecretKey claveSecreta;
    private final long expiracion;

    public JWTService(@Value("${jwt.secret}") String claveSecreta,
                      @Value("${jwt.expiration}") long expiracion) {
        byte[] bytes = Arrays.copyOf(claveSecreta.getBytes(StandardCharsets.UTF_8), 32);
        this.claveSecreta = Keys.hmacShaKeyFor(bytes);
        this.expiracion = expiracion;
    }

    public String generar(String nameuser, String rol) {
        Date actual = new Date();
        return Jwts.builder()
                .claim("rol", rol)
                .subject(nameuser)
                .issuedAt(actual)
                .expiration(new Date(actual.getTime() + expiracion))
                .signWith(claveSecreta)
                .compact();
    }

    public Claims leer(String token) {
        return Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
