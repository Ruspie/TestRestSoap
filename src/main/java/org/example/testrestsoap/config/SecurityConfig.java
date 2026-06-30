package org.example.testrestsoap.config;

import org.example.testrestsoap.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Включает @PreAuthorize("hasRole(...)") над методами контроллеров
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Отключаем CSRF для удобного тестирования через Swagger/Postman

            .authorizeHttpRequests(auth -> auth
                // 1. Полностью открываем Swagger UI и спецификацию OpenAPI
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                // 2. Открываем консоль H2 (если требуется для тестов)
                .requestMatchers("/h2-console/**").denyAll()
                // 3. Все запросы к /api/persons/** управляются аннотациями @PreAuthorize внутри контроллера,
                // но базово требуют быть просто аутентифицированным
                .requestMatchers("/api/persons/**").authenticated()
                // 4. Закрываем все остальные эндпоинты приложения
                .anyRequest().authenticated()
            )
            // Разрешаем отрисовку H2 консоли внутри фреймов страницы
            .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
            // Классическая сессионная форма логина Spring Security
            .formLogin(form -> form
                .defaultSuccessUrl("/swagger-ui/index.html", true) // Редирект в Swagger UI после успешного входа
                .permitAll()
            )
            // Сброс авторизации и уничтожение куки JSESSIONID
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
