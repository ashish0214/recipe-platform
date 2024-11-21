package com.epam.recipe.platform.service;

import com.epam.recipe.platform.config.RsaConfigurationProperties;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtEncoder encoder;
    private final RsaConfigurationProperties rsaConfigurationProperties;

    public String generateToken(Authentication authentication){
        Instant now = Instant.now();
        log.info("Generating token for user: {}", authentication.getName());
        String scopes = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("EPAM Systems INC")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .claim("scope", scopes)
                .build();
        log.info("Token generated successfully for user: {}", authentication.getName());
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token) {
        SignedJWT signedJWT = parseToken(token);
        try {
            JWSVerifier verifier = new RSASSAVerifier(rsaConfigurationProperties.publicKey());
            return signedJWT.verify(verifier);
        } catch (JOSEException e) {
            throw new UserException("Token verification error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected SignedJWT parseToken(String token) {
        try {
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new UserException("Invalid token format.", HttpStatus.BAD_REQUEST);
        }
    }
}
