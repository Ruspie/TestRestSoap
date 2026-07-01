package org.example.testrestsoap.config;

import lombok.RequiredArgsConstructor;
import org.example.testrestsoap.entity.jpa.UserEntity;
import org.example.testrestsoap.service.CustomUserDetailsService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecureConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Без сессий
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "swagger-ui.html",
                                "/api/auth/**" // Разрешаем логин и рефреш всем
                        ).permitAll()
                        .requestMatchers("/h2-console/**").denyAll()
                        .anyRequest().authenticated() // Все остальные эндпоинты закрыты, роли проверяются в контроллерах
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public CommandLineRunner initUsers(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        return args -> {

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                UserEntity user = new UserEntity();

                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole("USER");

                session.persist(user);

                UserEntity admin = new UserEntity();

                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole("ADMIN");

                session.persist(admin);

                UserEntity test = new UserEntity();

                test.setUsername("test");
                test.setPassword(passwordEncoder.encode("password"));
                test.setRole("TEST");

                session.persist(test);

                session.getTransaction().commit();
            }
        };
    }

}
