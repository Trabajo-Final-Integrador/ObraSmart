package com.ObraSmart.Dto;

import com.ObraSmart.Entity.Role;
import com.ObraSmart.Entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;      // 👈 agregado
    private String password;   // 👈 agregado (opcional si no se usa en este microservicio)
    private Status status;
    private Role role;
}
