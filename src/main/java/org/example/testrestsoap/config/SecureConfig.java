package org.example.testrestsoap.config;

import org.example.testrestsoap.entity.jpa.UserEntity;
import org.example.testrestsoap.service.CustomUserDetailsService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecureConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/h2-console/**").denyAll()
                        .requestMatchers("/api/persons").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/persons/{id}/age").hasRole("ADMIN")
                        .requestMatchers("/api/persons/**").authenticated()
                        .anyRequest().denyAll()

                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/swagger-ui/index.html", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
