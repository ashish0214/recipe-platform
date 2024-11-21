package com.epam.recipe.platform.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserEntityTest {

    private UserEntity user;

    @BeforeEach
    public void setup() {
        user = new UserEntity();
    }

    @Test
    public void testGettersAndSetters() {
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setImageUrl("http://example.com/image.jpg");

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setRoleName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);

        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("http://example.com/image.jpg", user.getImageUrl());
        assertEquals(roles, user.getRoles());
    }



    @Test
    public void testUserDetailsMethods() {
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        assertEquals("", user.getUsername()); // Assuming getUsername returns an empty string as per the current implementation
    }
}
