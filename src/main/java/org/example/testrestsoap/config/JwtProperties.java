package org.example.testrestsoap.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    private String secret;
    private Duration accessTokenExpiration;
    private Duration refreshTokenExpiration;
    private List<EndpointRule> endpoints;

    @Getter
    @Setter
    public static class EndpointRule {
        private String pattern;
        private List<String> methods;
        private List<String> roles;
    }

}
