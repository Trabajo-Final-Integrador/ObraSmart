package com.ObraSmart.GestionReportes.service.impl;

import com.ObraSmart.GestionReportes.dto.DashboardReporteDto;
import com.ObraSmart.GestionReportes.dto.DashboardReporteDto.*;


// DTOs externos reales
import com.ObraSmart.GestionReportes.dto.externos.EquipoDTO;
import com.ObraSmart.GestionReportes.dto.externos.ReparacionResponseDto;
import com.ObraSmart.GestionReportes.dto.externos.RepuestoDto;
import com.ObraSmart.GestionReportes.dto.externos.MovimientoStockDto;


import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements com.ObraSmart.GestionReportes.service.ReporteService {

    private final RestTemplate restTemplate;

    // ============================
    //  URLs DEL GATEWAY
    // ============================
    @Value("${obrasmart.equipos.base-url}")
    private String equiposBaseUrl;

    @Value("${obrasmart.reparaciones.base-url}")
    private String reparacionesBaseUrl;

    @Value("${obrasmart.stock.repuestos-url}")
    private String repuestosUrl;

    @Value("${obrasmart.stock.movimientos-url}")
    private String movimientosUrl;


    // ============================
    //  MÉTODO PRINCIPAL
    // ============================
    @Override
    public DashboardReporteDto generarDashboard() {

        log.info("🔄 Generando dashboard de ObraSmart…");

        // 1) Consumimos los microservicios vía API Gateway
        List<EquipoDTO> equipos = obtenerEquipos();
        List<ReparacionResponseDto> reparaciones = obtenerReparaciones();
        List<RepuestoDto> repuestos = obtenerRepuestos();
        List<MovimientoStockDto> movimientos = obtenerMovimientos();

        // 2) Construimos cada sección
        EquiposDashboardDto equiposDto = construirSeccionEquipos(equipos, reparaciones);
        ReparacionesDashboardDto reparacionesDto = construirSeccionReparaciones(reparaciones);
        StockDashboardDto stockDto = construirSeccionStock(repuestos, movimientos);

        // 3) Calculamos estado general
        EstadoGeneralDto estadoGeneral = calcularEstadoGeneral(equiposDto, reparacionesDto, stockDto);

        DashboardReporteDto dashboard = DashboardReporteDto.builder()
                .estadoGeneral(estadoGeneral)
                .equipos(equiposDto)
                .reparaciones(reparacionesDto)
                .stock(stockDto)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        log.info("✅ Dashboard generado correctamente.");
        return dashboard;
    }


    // ============================
    //  CONSUMO DE MICROSERVICIOS
    // ============================

    private List<EquipoDTO> obtenerEquipos() {
        try {
            ResponseEntity<List<EquipoDTO>> response = restTemplate.exchange(
                    equiposBaseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<EquipoDTO>>() {}
            );
            return Optional.ofNullable(response.getBody()).orElse(List.of());

        } catch (Exception e) {
            log.error("❌ Error consultando Equipos", e);
            return List.of();
        }
    }

    private List<ReparacionResponseDto> obtenerReparaciones() {
        try {
            log.info("🔍 Consultando reparaciones en: {}", reparacionesBaseUrl);

            var response = exchangeConCookie(
                    reparacionesBaseUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<List<ReparacionResponseDto>>() {}
            );
            var lista = Optional.ofNullable(response.getBody()).orElse(List.of());
            log.info("✅ Reparaciones obtenidas: {}", lista.size());
            return lista;

        } catch (Exception e) {
            log.error("❌ Error consultando Reparaciones: {}", e.getMessage());
            return List.of();
        }
    }

    private List<RepuestoDto> obtenerRepuestos() {
        try {
            log.info("🔍 Consultando repuestos en: {}", repuestosUrl);

            var response = exchangeConCookie(
                    repuestosUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<List<RepuestoDto>>() {}
            );
            var lista = Optional.ofNullable(response.getBody()).orElse(List.of());
            log.info("✅ Repuestos obtenidos: {}", lista.size());
            return lista;

        } catch (Exception e) {
            log.error("❌ Error consultando Repuestos: {}", e.getMessage());
            return List.of();
        }
    }


    private List<MovimientoStockDto> obtenerMovimientos() {
        try {
            log.info("🔍 Consultando movimientos en: {}", movimientosUrl);

            var response = exchangeConCookie(
                    movimientosUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<List<MovimientoStockDto>>() {}
            );
            var lista = Optional.ofNullable(response.getBody()).orElse(List.of());
            log.info("✅ Movimientos obtenidos: {}", lista.size());
            return lista;

        } catch (Exception e) {
            log.error("❌ Error consultando Movimientos: {}", e.getMessage());
            return List.of();
        }
    }



    // ============================
    //  SECCIÓN EQUIPOS
    // ============================

    private EquiposDashboardDto construirSeccionEquipos(List<EquipoDTO> equipos,
                                                        List<ReparacionResponseDto> reparaciones) {

        int totalEquipos = equipos.size();

        // Crear un mapa de equipos por ID para búsqueda rápida
        Map<Long, EquipoDTO> equipoMap = equipos.stream()
                .collect(Collectors.toMap(EquipoDTO::id, e -> e));

        // Agrupar por tipo (usando nombre del equipo como proxy del tipo)
        Map<String, Long> porTipo = equipos.stream()
                .collect(Collectors.groupingBy(
                        e -> safeToString(e.nombre()),
                        Collectors.counting()
                ));

        Map<String, Long> porMarca = equipos.stream()
                .collect(Collectors.groupingBy(
                        e -> "Marca " + safeToString(e.idMarca()),
                        Collectors.counting()
                ));

        Map<String, Long> porModelo = equipos.stream()
                .collect(Collectors.groupingBy(
                        e -> "Modelo " + safeToString(e.idModelo()),
                        Collectors.counting()
                ));

        Map<String, Long> porCombustible = equipos.stream()
                .collect(Collectors.groupingBy(
                        e -> safeToString(e.combustible()),
                        Collectors.counting()
                ));


        // Ranking equipos más críticos - agrupar por equipoId real
        Map<Long, Long> reparacionesPorEquipo = reparaciones.stream()
                .filter(r -> r.equipoId() != null)
                .collect(Collectors.groupingBy(
                        ReparacionResponseDto::equipoId,
                        Collectors.counting()
                ));

        List<EquipoCriticoDto> equiposCriticos = reparacionesPorEquipo.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    EquipoDTO equipo = equipoMap.get(entry.getKey());
                    return EquipoCriticoDto.builder()
                            .equipoId(entry.getKey())
                            .nombreEquipo(equipo != null ? equipo.nombre() : "Equipo " + entry.getKey())
                            .cantidadReparaciones(entry.getValue())
                            .build();
                })
                .toList();

        return EquiposDashboardDto.builder()
                .totalEquipos(totalEquipos)
                .equiposPorTipo(porTipo)
                .equiposPorMarca(porMarca)
                .equiposPorModelo(porModelo)
                .equiposPorCombustible(porCombustible)
                .equiposCriticos(equiposCriticos)
                .build();
    }


    // ============================
    //  SECCIÓN REPARACIONES
    // ============================

    private ReparacionesDashboardDto construirSeccionReparaciones(List<ReparacionResponseDto> reparaciones) {

        int totalReparaciones = reparaciones.size();

        Map<String, Long> porEstado = reparaciones.stream()
                .collect(Collectors.groupingBy(
                        r -> safeToString(r.estadoReparacion()),
                        Collectors.counting()
                ));

        Map<String, Long> porTipoMantenimiento = reparaciones.stream()
                .collect(Collectors.groupingBy(
                        r -> safeToString(r.tipoMantenimiento()),
                        Collectors.counting()
                ));

        Map<String, Long> reparacionesPorMes = reparaciones.stream()
                .filter(r -> r.fechaCreacion() != null)
                .collect(Collectors.groupingBy(
                        r -> r.fechaCreacion().getYear() +
                                "-" + String.format("%02d", r.fechaCreacion().getMonthValue()),
                        Collectors.counting()
                ));

        Map<Long, Long> reparacionesPorEquipo = reparaciones.stream()
                .filter(r -> r.equipoId() != null)
                .collect(Collectors.groupingBy(
                        ReparacionResponseDto::equipoId,
                        Collectors.counting()
                ));

        List<EquipoCriticoDto> topEquipos = reparacionesPorEquipo.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(x -> new EquipoCriticoDto(x.getKey(),
                        "Equipo " + x.getKey(),
                        x.getValue()))
                .toList();

        double tiempoPromedioHoras = reparaciones.stream()
                .filter(r -> r.fechaInicio() != null && r.fechaFin() != null)
                .mapToLong(r -> java.time.Duration.between(r.fechaInicio(), r.fechaFin()).toHours())
                .average()
                .orElse(0.0);

        return ReparacionesDashboardDto.builder()
                .totalReparaciones(totalReparaciones)
                .reparacionesPorEstado(porEstado)
                .reparacionesPorTipoMantenimiento(porTipoMantenimiento)
                .tiempoPromedioHoras(tiempoPromedioHoras)
                .reparacionesPorMes(reparacionesPorMes)
                .reparacionesPorEquipo(reparacionesPorEquipo)
                .topEquiposConMasFallas(topEquipos)
                .build();
    }


    // ============================
    //  SECCIÓN STOCK
    // ============================

    private StockDashboardDto construirSeccionStock(List<RepuestoDto> repuestos,
                                                    List<MovimientoStockDto> movimientos) {

        int totalRepuestos = repuestos.size();

        // Repuestos bajo mínimo
        List<RepuestoCriticoDto> repuestosCriticos = repuestos.stream()
                .filter(r -> r.stock() != null && r.stockMinimo() != null && r.stock() < r.stockMinimo())
                .map(r -> RepuestoCriticoDto.builder()
                        .repuestoId(r.id())
                        .nombre(r.nombre())
                        .categoria("Categoría " + safeToString(r.idCategoria()))
                        .stockActual(r.stock())
                        .stockMinimo(r.stockMinimo())
                        .build())
                .limit(10)
                .toList();

        int repuestosBajoMinimo = (int) repuestos.stream()
                .filter(r -> r.stock() != null && r.stockMinimo() != null && r.stock() < r.stockMinimo())
                .count();

        // Agrupar por categoría
        Map<String, Long> repuestosPorCategoria = repuestos.stream()
                .collect(Collectors.groupingBy(
                        r -> "Categoría " + safeToString(r.idCategoria()),
                        Collectors.counting()
                ));

        Map<String, Long> movimientosPorTipo = movimientos.stream()
                .collect(Collectors.groupingBy(
                        m -> safeToString(m.tipo()),
                        Collectors.counting()
                ));

        Map<String, Long> movimientosPorMes = new HashMap<>();

        return StockDashboardDto.builder()
                .totalRepuestos(totalRepuestos)
                .repuestosBajoMinimo(repuestosBajoMinimo)
                .repuestosCriticos(repuestosCriticos)
                .repuestosPorCategoria(repuestosPorCategoria)
                .movimientosPorTipo(movimientosPorTipo)
                .movimientosPorMes(movimientosPorMes)
                .build();
    }


    // ============================
    //  ESTADO GENERAL
    // ============================

    private EstadoGeneralDto calcularEstadoGeneral(EquiposDashboardDto equipos,
                                                   ReparacionesDashboardDto reparaciones,
                                                   StockDashboardDto stock) {

        int equiposCriticos = Optional.ofNullable(equipos.getEquiposCriticos()).orElse(List.of()).size();
        int repuestosCriticos = stock.getRepuestosBajoMinimo();

        // Contar reparaciones abiertas/activas
        Map<String, Long> estadosReparaciones = reparaciones.getReparacionesPorEstado();
        int reparacionesAbiertas = estadosReparaciones.entrySet().stream()
                .filter(e -> e.getKey().contains("ABIERTA") ||
                           e.getKey().contains("EN_PROCESO") ||
                           e.getKey().contains("PENDIENTE"))
                .mapToInt(e -> e.getValue().intValue())
                .sum();

        // Calcular nivel de riesgo
        String nivelRiesgo;
        String mensaje;

        int totalProblemas = equiposCriticos + repuestosCriticos + reparacionesAbiertas;

        if (totalProblemas >= 10) {
            nivelRiesgo = "ALTO";
            mensaje = "Atención requerida: múltiples equipos críticos, stock bajo y reparaciones pendientes";
        } else if (totalProblemas >= 5) {
            nivelRiesgo = "MEDIO";
            mensaje = "Monitoreo necesario: algunos equipos requieren atención";
        } else {
            nivelRiesgo = "BAJO";
            mensaje = "Sistema funcionando correctamente";
        }

        return EstadoGeneralDto.builder()
                .equiposCriticos(equiposCriticos)
                .repuestosCriticos(repuestosCriticos)
                .reparacionesAbiertas(reparacionesAbiertas)
                .nivelRiesgo(nivelRiesgo)
                .mensaje(mensaje)
                .build();
    }


    // ============================
    //  HELPER
    // ============================

    private String safeToString(Object value) {
        return value != null ? value.toString() : "N/D";
    }
    private String obtenerCookieSesion() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) return null;

        var request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("JSESSIONID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private <T> ResponseEntity<T> exchangeConCookie(String url, HttpMethod method, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();

        String cookie = obtenerCookieSesion();
        if (cookie != null) {
            headers.set("Cookie", "JSESSIONID=" + cookie);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, method, entity, responseType);
    }

}
