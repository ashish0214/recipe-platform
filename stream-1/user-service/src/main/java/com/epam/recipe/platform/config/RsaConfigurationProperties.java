package com.epam.recipe.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@SuppressWarnings("all")
@ConfigurationProperties(prefix = "rsa")
public record RsaConfigurationProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
