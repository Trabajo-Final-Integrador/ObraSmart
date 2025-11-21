package com.ObraSmart.GestionEquipos.dtos;

import com.ObraSmart.GestionEquipos.entity.Combustible;
import com.ObraSmart.GestionEquipos.entity.Estado_Operativo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoDTO {

    private Long id; // opcional al crear, útil para update o response

    // NUEVO: código interno. Si viene null/blank → se genera en el service.
    @Pattern(
            regexp = "^[A-Z]{1,4}\\d{4}$",
            message = "El código interno debe tener formato PREFIJO + 4 dígitos (ej: RET0001)"
    )
    private String codigoInterno;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de equipo es obligatorio")
    private Long idTipoEquipo; // solo se envía el id, no el objeto completo

    @NotNull(message = "La marca es obligatoria")
    private Long idMarca;

    @NotNull(message = "El modelo es obligatorio")
    private Long idModelo;

    @NotBlank(message = "El número de serie es obligatorio")
    private String numeroSerie;

    @NotNull(message = "El año de fabricación es obligatorio")
    @Min(value = 2000, message = "El año de fabricación debe ser mayor a 2000")
    @Max(value = 2025, message = "El año de fabricación debe ser menor a 2025")
    private Integer anioFabricacion;

    @NotNull(message = "La potencia del motor es obligatoria")
    @Positive(message = "La potencia debe ser un número positivo")
    private Double potenciaHp;

    @NotNull(message = "El tipo de combustible es obligatorio")
    private Combustible combustible;

    @NotNull(message = "El estado operativo es obligatorio")
    private Estado_Operativo estadoOperativo;

    @NotNull(message = "El kilometraje o las horas de uso son obligatorios")
    @PositiveOrZero(message = "El kilometraje o las horas de uso deben ser mayores o iguales a cero")
    private Double kilometrajeHorasUso;

    @NotNull(message = "La fecha del último mantenimiento es obligatoria")
    @PastOrPresent(message = "La fecha del último mantenimiento no puede ser futura")
    private LocalDate fechaUltimoMantenimiento;

    @NotNull(message = "La fecha del próximo mantenimiento es obligatoria")
    @Future(message = "La fecha del próximo mantenimiento debe ser futura")
    private LocalDate proximoMantenimiento;

    @NotBlank(message = "El responsable de mantenimiento es obligatorio")
    private String responsableMantenimiento;

    @Pattern(regexp = "^[A-Z]{2,3}\\d{3,4}[A-Z]{0,1}$", message = "La patente no tiene un formato válido")
    private String numeroPatente; // opcional

    @NotNull(message = "Debe indicar si el seguro está vigente")
    private Boolean seguroVigente;

    @FutureOrPresent(message = "La fecha de vencimiento del seguro debe ser actual o futura")
    private LocalDate fechaVencimientoSeguro; // opcional si seguroVigente = true

    @NotBlank(message = "La ubicación actual del equipo es obligatoria")
    private String ubicacionActual;

    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean activo;

    private Double latitud;

    private Double longitud;
}
