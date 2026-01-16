package com.obrasmart.gestionauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador de login principal
    @Column(nullable = false, unique = true, length = 80)
    private String username;

    // Permite login alternativo por email y recuperación de contraseña
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    // Hash BCrypt de la contraseña
    @Column(nullable = false)
    private String passwordHash;

    // Rol de negocio (se usa como autoridad en Spring Security)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    // Estado de la cuenta (ACTIVO / INACTIVO)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
}
