package com.example.cadastrousuarios.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class SecurityProperties {

    private String secret;
    private long expirationInMs;

    // Getters e Setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationInMs() {
        return expirationInMs;
    }

    public void setExpirationInMs(long expirationInMs) {
        this.expirationInMs = expirationInMs;
    }
}