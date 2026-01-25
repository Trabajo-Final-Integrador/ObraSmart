package com.ObraSmart.GestionReportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO principal del dashboard de ObraSmart.
 * Agrupa secciones de Equipos, Reparaciones, Stock
 * + un estado general de riesgo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardReporteDto {

    private EstadoGeneralDto estadoGeneral;
    private EquiposDashboardDto equipos;
    private ReparacionesDashboardDto reparaciones;
    private StockDashboardDto stock;
    private LocalDateTime fechaGeneracion;

    // ============================
    //  ESTADO GENERAL
    // ============================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EstadoGeneralDto {
        private int equiposCriticos;
        private int repuestosCriticos;
        private int reparacionesAbiertas;
        private String nivelRiesgo;   // "BAJO", "MEDIO", "ALTO"
        private String mensaje;       // mensaje explicativo
    }

    // ============================
    //  SECCIÓN EQUIPOS
    // ============================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EquiposDashboardDto {
        private int totalEquipos;
        private Map<String, Long> equiposPorTipo;        // { "Retroexcavadora": 5, ... }
        private Map<String, Long> equiposPorMarca;       // { "Caterpillar": 3, ... }
        private Map<String, Long> equiposPorModelo;      // { "320D": 2, ... }
        private Map<String, Long> equiposPorCombustible; // { "Diesel": 10, "Nafta": 3 }

        /**
         * Ranking de equipos más problemáticos:
         * lista de (idEquipo, nombre, cantidadReparaciones)
         */
        private List<EquipoCriticoDto> equiposCriticos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EquipoCriticoDto {
        private Long equipoId;
        private String nombreEquipo;
        private long cantidadReparaciones;
    }

    // ============================
    //  SECCIÓN REPARACIONES
    // ============================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReparacionesDashboardDto {
        private int totalReparaciones;
        private Map<String, Long> reparacionesPorEstado;          // { "ABIERTA": 3, "EN_PROCESO": 2, ... }
        private Map<String, Long> reparacionesPorTipoMantenimiento; // { "PREVENTIVO": 4, "CORRECTIVO": 5 }
        private double tiempoPromedioHoras;                       // promedio entre inicio/fin
        private Map<Long, Long> reparacionesPorEquipo;            // { equipoId: cantidad }
        private Map<String, Long> reparacionesPorMes;             // { "2025-01": 12, ... }
        private List<EquipoCriticoDto> topEquiposConMasFallas;    // reutilizamos el mismo DTO
    }

    // ============================
    //  SECCIÓN STOCK / REPUESTOS
    // ============================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StockDashboardDto {
        private int totalRepuestos;
        private int repuestosBajoMinimo;
        private List<RepuestoCriticoDto> repuestosCriticos;   // los más usados y debajo de mínimo
        private Map<String, Long> repuestosPorCategoria;      // { "Filtros": 10, "Aceites": 5 }
        private Map<String, Long> movimientosPorTipo;         // { "ENTRADA": 15, "SALIDA": 20 }
        private Map<String, Long> movimientosPorMes;          // { "2025-01": 30, ... }
        private List<RepuestoUsoDto> repuestosMasUsados;      // ranking de uso
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RepuestoCriticoDto {
        private Long repuestoId;
        private String nombre;
        private String categoria;
        private int stockActual;
        private int stockMinimo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RepuestoUsoDto {
        private Long repuestoId;
        private String nombre;
        private String categoria;
        private long cantidadMovimientosSalida;
    }
}
