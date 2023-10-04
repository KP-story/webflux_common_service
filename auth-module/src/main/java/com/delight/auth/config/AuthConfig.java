package com.delight.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {
    private int idTokenExpiration;
    private int refreshTokenExpiration;
    private String privateKey;

    private byte[] privateKeyBytes;
    private String issuer;
    private String kId;

    public byte[] getPrivateKeyBytes() {
        if (privateKeyBytes == null) {
            privateKeyBytes = privateKey.getBytes(StandardCharsets.UTF_8);
        }
        return privateKeyBytes;
    }
}
