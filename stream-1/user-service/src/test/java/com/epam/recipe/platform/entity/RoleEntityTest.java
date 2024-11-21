package com.epam.recipe.platform.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoleEntityTest {

    private RoleEntity roleEntity;

    @BeforeEach
    public void setUp() {
        roleEntity = new RoleEntity();
    }

    @Test
    public void testGettersAndSetters() {
        roleEntity.setId(1L);
        roleEntity.setRoleName("ROLE_USER");

        assertEquals(1L, roleEntity.getId());
        assertEquals("ROLE_USER", roleEntity.getRoleName());
    }

    @Test
    public void testNoArgsConstructor() {
        RoleEntity entity = new RoleEntity();
        assertNotNull(entity);
    }

    @Test
    public void testAllArgsConstructor() {
        Set<UserEntity> users = new HashSet<>();
        RoleEntity entity = new RoleEntity(1L, "ROLE_ADMIN", users);

        assertEquals(1L, entity.getId());
        assertEquals("ROLE_ADMIN", entity.getRoleName());
        assertEquals(users, entity.getUsers());
    }

    @Test
    public void testBuilder() {
        Set<UserEntity> users = new HashSet<>();
        RoleEntity entity = RoleEntity.builder()
                .id(1L)
                .roleName("ROLE_MANAGER")
                .users(users)
                .build();

        assertEquals(1L, entity.getId());
        assertEquals("ROLE_MANAGER", entity.getRoleName());
        assertEquals(users, entity.getUsers());
    }
}
