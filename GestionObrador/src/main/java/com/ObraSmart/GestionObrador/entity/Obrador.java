package com.ObraSmart.GestionObrador.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "obradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Obrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String ubicacion;

    @Column(name = "supervisor_user_id")
    private Long supervisorUserId;

    @ElementCollection
    @CollectionTable(name = "obrador_equipos", joinColumns = @JoinColumn(name = "obrador_id"))
    @Column(name = "equipo_id")
    @Builder.Default
    private Set<Long> equipoIds = new HashSet<>();
}
