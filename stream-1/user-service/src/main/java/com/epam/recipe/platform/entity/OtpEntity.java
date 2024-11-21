package com.epam.recipe.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "otp_entity")
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @Column(name = "email", unique = true)
    private  String email;

    @Column(name="otp")
    private Integer otp;

    @Column(name = "expiration_time")
    private Date expirationTime;

}
