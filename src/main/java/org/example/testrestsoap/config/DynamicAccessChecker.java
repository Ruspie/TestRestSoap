package org.example.testrestsoap.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

@Component("accessChecker")
@RequiredArgsConstructor
public class DynamicAccessChecker {

    private final JwtProperties jwtProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean check(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Пробегаемся по правилам из application-access.yaml
        for (JwtProperties.EndpointRule rule : jwtProperties.getEndpoints()) {
            if (pathMatcher.match(rule.getPattern(), requestUri)) {
                if (rule.getMethods() == null || rule.getMethods().contains(requestMethod)) {

                    // Проверяем роли пользователя
                    for (String requiredRole : rule.getRoles()) {
                        String expectedAuthority = "ROLE_" + requiredRole;
                        boolean hasRole = authorities.stream()
                                .anyMatch(auth -> auth.getAuthority().equals(expectedAuthority));
                        if (hasRole) {
                            return true; // Найдено совпадение — доступ разрешен
                        }
                    }
                    return false; // Путь совпал, но у пользователя нет нужной роли
                }
            }
        }

        // Если эндпоинт вообще не описан в YAML, по умолчанию закрываем доступ (безопасный подход)
        return false;
    }
}