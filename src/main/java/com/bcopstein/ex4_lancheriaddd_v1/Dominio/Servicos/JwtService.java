package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;
 
import java.util.Date;
 
import javax.crypto.SecretKey;
 
import org.springframework.stereotype.Service;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
 
@Service
public class JwtService {
 
    private static final String SECRET = "chave-de-seguranca-pizzaria-precisa-de-muitos-caracteres";
    private static final long PERIODO_EXPIRA = 1000L * 60 * 60 * 24; 
 
    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
 
    public String geraToken(String email) {
        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + PERIODO_EXPIRA))
            .signWith(getChave())
            .compact();
    }
 
    public String extraiEmail(String token) {
        return getClaims(token).getSubject();
    }
 
    public boolean tokenValido(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
 
    private Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(getChave())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
