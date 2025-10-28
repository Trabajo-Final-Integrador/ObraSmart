package com.ObraSmart.GestionInspeccion.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empleados")
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;

    @Column(unique = true)
    @NotBlank
    private String dni;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String direccion;
    @NotBlank
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] carnetFoto;
}
