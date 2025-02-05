package com.example.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.token.validity.ms}")
  private long jwtTokenValidityMs;

  @Value("${jwt.secret}")
  private String jwtSecret;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidityMs))
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

}


