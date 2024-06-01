package com.akatsuki.pioms.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${spring.jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUserId(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    public String getCategory(String token) {
        return getAllClaimsFromToken(token).get("category", String.class);
    }

    public String getRole(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    public Integer getUserCode(String token) {
        return getAllClaimsFromToken(token).get("usercode", Integer.class);
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).get("username", String.class);
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        String role = getRole(token);
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public String createJwt(String category, int usercode, String userId, String username, String role, long expiration) {
        return Jwts.builder()
                .claim("category", category)
                .claim("usercode", usercode)
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}
