package org.example.testrestsoap.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.config.JwtProperties;
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
    private final JwtProperties jwtProperties; // Внедряем конфигурацию из YAML

    public String generateAccessToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());

        List<String> rolesAndAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration().toMillis()))
                .withClaim("roles", rolesAndAuthorities)
                .sign(algorithm);
    }

    @Transactional
    public String generateRefreshToken(String username) {
        Session session = sessionFactory.getCurrentSession();

        session.createMutationQuery("DELETE FROM RefreshTokenEntity WHERE username = :username")
                .setParameter("username", username)
                .executeUpdate();

        String token = UUID.randomUUID().toString();
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(token);
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(Instant.now().plus(jwtProperties.getRefreshTokenExpiration())); // Берем Duration из YAML

        session.persist(refreshToken);
        return token;
    }

    public DecodedJWT verifyAccessToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
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
