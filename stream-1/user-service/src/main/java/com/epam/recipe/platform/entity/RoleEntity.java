package com.epam.recipe.platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Role_Name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;
}
