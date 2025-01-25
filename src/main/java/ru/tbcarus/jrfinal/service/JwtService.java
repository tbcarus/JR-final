package ru.tbcarus.jrfinal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.tbcarus.jrfinal.model.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    private final long expirationTime = 20 * 1000;

    private final long refreshExpirationTime = 7 * 24 * 60 * 60 * 1000;


    private  Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        return claims;
    }

//Генерация токена
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .claims(generateClaims(user))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey()).compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .claims(generateClaims(user))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(getSigningKey()).compact();
    }

    public String refreshAccessToken(User user) {
        return generateAccessToken(user);
    }


//Проверка токена на валидность
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

//Извлечение имени пользователя из токена
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//Извлечение данных из токена
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

//Проверка токена на просроченность
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

//Извлечение даты истечения токена
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

//Извлечение всех данных из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

//Получение ключа для подписи токена
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSigningKey));
    }
}
