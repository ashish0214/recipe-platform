package com.epam.recipe.platform.service;

import com.epam.recipe.platform.config.RsaConfigurationProperties;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import java.security.interfaces.RSAPublicKey;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private RsaConfigurationProperties rsaConfigurationProperties;

    @Mock
    private SignedJWT signedJWT;

    @Mock
    private RSAPublicKey publicKey;

    @Spy
    @InjectMocks
    private TokenService tokenService;

    @Test
    public void testGenerateToken() {

        when(authentication.getName()).thenReturn("example@test.com");

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("scope", "ROLE_USER")
                .build();
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        String result = tokenService.generateToken(authentication);
        assertEquals("token", result);
        verify(jwtEncoder, times(1)).encode(any());
        verify(authentication, times(1)).getAuthorities();
        verify(authentication, times(2)).getName();
    }

    @Test
    public void whenTokenValid_thenReturnTrue() throws Exception {
        doReturn(signedJWT).when(tokenService).parseToken(anyString());
        doReturn(publicKey).when(rsaConfigurationProperties).publicKey();
        when(signedJWT.verify(any(JWSVerifier.class))).thenReturn(true);
        assertTrue(tokenService.validateToken("dummy.jwt.token"));
    }

    @Test
    public void whenTokenInvalid_thenReturnFalse() throws Exception {
        Mockito.doReturn(signedJWT).when(tokenService).parseToken(anyString());
        Mockito.doReturn(publicKey).when(rsaConfigurationProperties).publicKey();
        Mockito.when(signedJWT.verify(any(JWSVerifier.class))).thenReturn(false);
        assertFalse(tokenService.validateToken("dummy.jwt.token"));
    }

    @Test
    public void whenJOSEException_thenReturnInternalError() throws Exception {
        Mockito.doReturn(signedJWT).when(tokenService).parseToken(anyString());
        Mockito.doReturn(publicKey).when(rsaConfigurationProperties).publicKey();
        Mockito.when(signedJWT.verify(any(JWSVerifier.class))).thenThrow(JOSEException.class);
        UserException ex = assertThrows(UserException.class, () -> tokenService.validateToken("dummy.jwt.token"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        assertEquals("Token verification error.", ex.getMessage());
    }

//    @Test
//    public void whenTokenParseException_thenThrowsUserException() throws Exception {
//        Mockito.doReturn(signedJWT).when(tokenService).parseToken(anyString());
//        Mockito.when(SignedJWT.parse(anyString())).thenThrow(ParseException.class);
//        UserException ex = assertThrows(UserException.class, () -> tokenService.validateToken("dummy.jwt.token"));
//        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
//        assertEquals("Invalid token format.", ex.getMessage());
//    }
}