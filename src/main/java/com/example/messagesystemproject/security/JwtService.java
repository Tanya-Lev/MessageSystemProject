package com.example.messagesystemproject.security;

import com.example.messagesystemproject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;
    @Value("${token.access.expiration}")
    private long accessExpiration;//todo поменять token.access.expiration=900000
    @Value("${token.refresh.expiration}")
    private long refreshExpiration;

    // Генерация токена с полезными claims по умолчанию(вызывает нижний ↓ метод).
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof User user) {
            claims.put("id", user.getUserId().toString());
            claims.put("login", user.getLogin());
            claims.put("email", user.getEmail());
            // добаляю всё, что считаю нужным (НЕ ПАРОЛИ, КАРТЫ и тд.)
        }

        return generateToken(claims, userDetails);
    }

    // Генерация Access токена с claims (Когда нужна гибкость, кастомный payload, чтобы в случае необходимости вручную передать claims)
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, accessExpiration);
    }

    // Генерация Refresh токена с дополнительными claims (если нужно сохранить ID, email и т.д.)
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    // Он просто собирает(технически создаёт) JWT из готовых данных(в моём случае всегда с claims).
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Извлекаем логин (username) из токена
    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Проверяем, не истёк ли токен
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractLogin(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Внутренний метод: истёк ли токен
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Внутренний метод: достаём дату окончания действия токена
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Общий метод для извлечения информации из токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Внутренний метод: достаём все данные из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Ключ, используемый для подписи токена
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String stripBearerPrefix(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println(token);
            throw new IllegalArgumentException("Invalid Authorization header format. Expected header to start with 'Bearer '.");
        }
        return token.substring(7);
    }

    public String getUserIdFromToken(String token) {
        String stripped = stripBearerPrefix(token);
        String userId = extractClaim(stripped, claims -> claims.get("id", String.class));
        if (userId == null) throw new RuntimeException("Missing user ID in token");
        return userId;
    }
}