package com.ObraSmart.GestionReparaciones.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String status;

    // Método auxiliar para obtener el nombre completo
    // Usa username ya que el microservicio no devuelve nombre/apellido
    public String getNombreCompleto() {
        return username != null ? username : "";
    }
}
