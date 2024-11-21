package com.epam.recipe.platform.service;

import com.epam.recipe.platform.entity.UserEntity;
import com.epam.recipe.platform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsernameSuccess() {
        String email = "testEmail@email.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword("password");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        assertEquals(user,userDetails);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsernameFail() {
        String email = "testEmail";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }
}
