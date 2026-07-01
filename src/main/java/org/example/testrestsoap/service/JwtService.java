package org.example.testrestsoap.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.RefreshTokenEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SessionFactory sessionFactory;

    private final String SECRET = "9a75v3b8c2d1e4f5g6h7j8k9l0m1n2p3r4s5t6u7v8w9x0z";
    private final Duration ACCESS_TOKEN_LIFETIME = Duration.ofMinutes(15);  // Срок жизни Access Token
    private final Duration REFRESH_TOKEN_LIFETIME = Duration.ofDays(7);     // Срок жизни Refresh Token

    public String generateAccessToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        List<String> rolesAndAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_LIFETIME.toMillis()))
                .withClaim("roles", rolesAndAuthorities) // Роли и кастомные Authorities в одном массиве
                .sign(algorithm);
    }

    @Transactional
    public String generateRefreshToken(String username) {
        Session session = sessionFactory.getCurrentSession();

        // Удаляем старые рефреш токены пользователя, чтобы не плодить сессии
        session.createMutationQuery("DELETE FROM RefreshTokenEntity WHERE username = :username")
                .setParameter("username", username)
                .executeUpdate();

        String token = UUID.randomUUID().toString();
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(token);
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_LIFETIME));

        session.persist(refreshToken);
        return token;
    }

    public DecodedJWT verifyAccessToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.require(algorithm).build().verify(token);
    }

    @Transactional(readOnly = true)
    public RefreshTokenEntity verifyRefreshToken(String token) {
        Session session = sessionFactory.getCurrentSession();
        RefreshTokenEntity refreshToken = session.createQuery(
                        "SELECT r FROM RefreshTokenEntity r WHERE r.token = :token", RefreshTokenEntity.class)
                .setParameter("token", token)
                .uniqueResult();

        if (refreshToken == null) {
            throw new IllegalArgumentException("Данный Refresh Token не существует или был отозван!");
        }
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Срок действия Refresh Token истек. Войдите заново.");
        }
        return refreshToken;
    }
}