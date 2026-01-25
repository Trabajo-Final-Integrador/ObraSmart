package com.ObraSmart.GestionLogistica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "traslados_equipos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrasladoEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipo_id", nullable = false)
    private Long equipoId;

    @Column(name = "origen_obrador_id", nullable = false)
    private Long origenObradorId;

    @Column(name = "destino_obrador_id", nullable = false)
    private Long destinoObradorId;

    private LocalDateTime programadoPara;

    private LocalDateTime fechaCreacion;

    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTraslado estado;

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoTraslado.PENDIENTE;
        }
    }

    public enum EstadoTraslado {
        PENDIENTE,
        EN_PROCESO,
        COMPLETADO,
        CANCELADO
    }
}
