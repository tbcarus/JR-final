package ru.tbcarus.jrfinal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.tbcarus.jrfinal.exception.TokenRevokedException;
import ru.tbcarus.jrfinal.model.RefreshToken;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.repository.RefreshTokenRepository;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    private final long expirationTime = 20 * 1000;
    private final long refreshExpirationTime = 7 * 24 * 60 * 60 * 1000;

    private Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        return claims;
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .claims(generateClaims(user))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey()).compact();
    }

    public String generateRefreshToken(User user) {
        String refreshToken = Jwts.builder()
                .claims(generateClaims(user))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(getSigningKey()).compact();
        RefreshToken entityRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .userName(extractUserName(refreshToken))
                .expires(extractExpiration(refreshToken).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .revoked(false)
                .build();
        refreshTokenRepository.save(entityRefreshToken);
        return refreshToken;
    }

    public String refreshAccessToken(User user, String refreshToken) {
        RefreshToken tokenDb = getRefreshToken(refreshToken);
        if (tokenDb != null && tokenDb.isRevoked()) {
            throw new TokenRevokedException(refreshToken, "token revoked");
        }
        return generateAccessToken(user);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSigningKey));
    }

    public RefreshToken getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token).get();
    }
}
