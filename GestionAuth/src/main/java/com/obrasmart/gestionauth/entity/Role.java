package com.obrasmart.gestionauth.entity;

/**
 * Roles de negocio que se exponen como authorities de Spring Security (prefijo ROLE_).
 */
public enum Role {
    ADMINISTRACION,
    SUPERVISOR,
    MECANICO,
    TECNICO,
    OPERADOR
}
