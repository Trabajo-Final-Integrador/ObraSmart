package com.ObraSmart.GestionReportes.service.impl;

import com.ObraSmart.GestionReportes.service.ReporteService;
import com.ObraSmart.GestionReportes.dto.DashboardReporteDto;
import com.ObraSmart.GestionReportes.dto.DashboardReporteDto.*;
import com.ObraSmart.GestionReportes.dto.externos.EquipoDTO;
import com.ObraSmart.GestionReportes.dto.externos.MovimientoStockDto;
import com.ObraSmart.GestionReportes.dto.externos.ReparacionResponseDto;
import com.ObraSmart.GestionReportes.dto.externos.RepuestoDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final RestTemplate restTemplate;

    @Value("${obrasmart.equipos.base-url}")
    private String equiposBaseUrl;

    @Value("${obrasmart.reparaciones.base-url}")
    private String reparacionesBaseUrl;

    @Value("${obrasmart.stock.repuestos-url}")
    private String repuestosUrl;

    @Value("${obrasmart.stock.movimientos-url}")
    private String movimientosUrl;

    @Override
    public DashboardReporteDto generarDashboard(LocalDate startDate, LocalDate endDate, String preset) {

        log.info("Generando dashboard de ObraSmart con filtros de fecha");
        RangoFechas rango = resolverRangoFechas(startDate, endDate, preset);

        List<EquipoDTO> equipos = obtenerEquipos();
        List<ReparacionResponseDto> reparaciones = obtenerReparaciones();
        List<RepuestoDto> repuestos = obtenerRepuestos();
        List<MovimientoStockDto> movimientos = obtenerMovimientos();

        List<ReparacionResponseDto> reparacionesFiltradas = filtrarReparacionesPorFecha(reparaciones, rango);
        List<MovimientoStockDto> movimientosFiltrados = filtrarMovimientosPorFecha(movimientos, rango);

        EquiposDashboardDto equiposDto = construirSeccionEquipos(equipos, reparacionesFiltradas, reparaciones);
        ReparacionesDashboardDto reparacionesDto = construirSeccionReparaciones(reparacionesFiltradas, reparaciones);
        StockDashboardDto stockDto = construirSeccionStock(repuestos, movimientosFiltrados);

        EstadoGeneralDto estadoGeneral = calcularEstadoGeneral(equiposDto, reparacionesDto, stockDto);

        DashboardReporteDto dashboard = DashboardReporteDto.builder()
                .estadoGeneral(estadoGeneral)
                .equipos(equiposDto)
                .reparaciones(reparacionesDto)
                .stock(stockDto)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        log.info("Dashboard generado correctamente ({} a {}).", rango.inicio(), rango.fin());
        return dashboard;
    }

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
            log.error("Error consultando Equipos", e);
            return List.of();
        }
    }

    private List<ReparacionResponseDto> obtenerReparaciones() {
        try {
            var response = exchangeConCookie(
                    reparacionesBaseUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<List<ReparacionResponseDto>>() {}
            );
            return Optional.ofNullable(response.getBody()).orElse(List.of());
        } catch (Exception e) {
            log.error("Error consultando Reparaciones: {}", e.getMessage());
            return List.of();
        }
    }

    private List<RepuestoDto> obtenerRepuestos() {
        try {
            var response = exchangeConCookie(
                    repuestosUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<List<RepuestoDto>>() {}
            );
            return Optional.ofNullable(response.getBody()).orElse(List.of());
        } catch (Exception e) {
            log.error("Error consultando Repuestos: {}", e.getMessage());
            return List.of();
        }
    }

    private List<MovimientoStockDto> obtenerMovimientos() {
        try {
            var response = exchangeConCookie(
                    movimientosUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<List<MovimientoStockDto>>() {}
            );
            return Optional.ofNullable(response.getBody()).orElse(List.of());
        } catch (Exception e) {
            log.error("Error consultando Movimientos: {}", e.getMessage());
            return List.of();
        }
    }

    private EquiposDashboardDto construirSeccionEquipos(List<EquipoDTO> equipos,
                                                        List<ReparacionResponseDto> reparacionesFiltradas,
                                                        List<ReparacionResponseDto> reparacionesOriginales) {

        int totalEquipos = equipos.size();
        Map<Long, EquipoDTO> equipoMap = equipos.stream()
                .collect(Collectors.toMap(EquipoDTO::id, e -> e));

        Map<String, Long> porTipo = equipos.stream()
                .collect(Collectors.groupingBy(e -> safeToString(e.nombre()), Collectors.counting()));

        Map<String, Long> porMarca = equipos.stream()
                .collect(Collectors.groupingBy(e -> "Marca " + safeToString(e.idMarca()), Collectors.counting()));

        Map<String, Long> porModelo = equipos.stream()
                .collect(Collectors.groupingBy(e -> "Modelo " + safeToString(e.idModelo()), Collectors.counting()));

        Map<String, Long> porCombustible = equipos.stream()
                .collect(Collectors.groupingBy(e -> safeToString(e.combustible()), Collectors.counting()));

        List<ReparacionResponseDto> baseParaRanking = reparacionesFiltradas.isEmpty()
                ? reparacionesOriginales
                : reparacionesFiltradas;

        Map<Long, Long> reparacionesPorEquipo = baseParaRanking.stream()
                .filter(r -> r.equipoId() != null)
                .collect(Collectors.groupingBy(ReparacionResponseDto::equipoId, Collectors.counting()));

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

    private ReparacionesDashboardDto construirSeccionReparaciones(List<ReparacionResponseDto> reparacionesFiltradas,
                                                                  List<ReparacionResponseDto> reparacionesOriginales) {

        List<ReparacionResponseDto> base = reparacionesFiltradas.isEmpty()
                ? reparacionesOriginales
                : reparacionesFiltradas;

        int totalReparaciones = base.size();

        Map<String, Long> porEstado = base.stream()
                .collect(Collectors.groupingBy(r -> safeToString(r.estadoReparacion()), Collectors.counting()));

        Map<String, Long> porTipoMantenimiento = base.stream()
                .collect(Collectors.groupingBy(r -> safeToString(r.tipoMantenimiento()), Collectors.counting()));

        Map<String, Long> reparacionesPorMes = base.stream()
                .filter(r -> r.fechaCreacion() != null)
                .collect(Collectors.groupingBy(
                        r -> r.fechaCreacion().getYear() + "-" + String.format("%02d", r.fechaCreacion().getMonthValue()),
                        Collectors.counting()
                ));

        Map<Long, Long> reparacionesPorEquipo = base.stream()
                .filter(r -> r.equipoId() != null)
                .collect(Collectors.groupingBy(ReparacionResponseDto::equipoId, Collectors.counting()));

        List<EquipoCriticoDto> topEquipos = reparacionesPorEquipo.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(x -> new EquipoCriticoDto(x.getKey(), "Equipo " + x.getKey(), x.getValue()))
                .toList();

        double tiempoPromedioHoras = base.stream()
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

    private StockDashboardDto construirSeccionStock(List<RepuestoDto> repuestos,
                                                    List<MovimientoStockDto> movimientos) {

        int totalRepuestos = repuestos.size();

        List<RepuestoCriticoDto> repuestosCriticos = repuestos.stream()
                .filter(r -> r.stock() != null && r.stockMinimo() != null && r.stock() < r.stockMinimo())
                .map(r -> RepuestoCriticoDto.builder()
                        .repuestoId(r.id())
                        .nombre(r.nombre())
                        .categoria("Categoria " + safeToString(r.idCategoria()))
                        .stockActual(r.stock())
                        .stockMinimo(r.stockMinimo())
                        .build())
                .limit(10)
                .toList();

        int repuestosBajoMinimo = (int) repuestos.stream()
                .filter(r -> r.stock() != null && r.stockMinimo() != null && r.stock() < r.stockMinimo())
                .count();

        Map<String, Long> repuestosPorCategoria = repuestos.stream()
                .collect(Collectors.groupingBy(r -> "Categoria " + safeToString(r.idCategoria()), Collectors.counting()));

        Map<String, Long> movimientosPorTipo = movimientos.stream()
                .collect(Collectors.groupingBy(m -> safeToString(m.tipo()), Collectors.counting()));

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

    private EstadoGeneralDto calcularEstadoGeneral(EquiposDashboardDto equipos,
                                                   ReparacionesDashboardDto reparaciones,
                                                   StockDashboardDto stock) {

        int equiposCriticos = Optional.ofNullable(equipos.getEquiposCriticos()).orElse(List.of()).size();
        int repuestosCriticos = stock.getRepuestosBajoMinimo();

        Map<String, Long> estadosReparaciones = reparaciones.getReparacionesPorEstado();
        int reparacionesAbiertas = estadosReparaciones.entrySet().stream()
                .filter(e -> e.getKey().contains("ABIERTA") || e.getKey().contains("EN_PROCESO") || e.getKey().contains("PENDIENTE"))
                .mapToInt(e -> e.getValue().intValue())
                .sum();

        String nivelRiesgo;
        String mensaje;

        int totalProblemas = equiposCriticos + repuestosCriticos + reparacionesAbiertas;

        if (totalProblemas >= 10) {
            nivelRiesgo = "ALTO";
            mensaje = "AtenciÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³n requerida: mÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âºltiples equipos crÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ticos, stock bajo y reparaciones pendientes";
        } else if (totalProblemas >= 5) {
            nivelRiesgo = "MEDIO";
            mensaje = "Monitoreo necesario: algunos equipos requieren atenciÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³n";
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

    private record RangoFechas(LocalDateTime inicio, LocalDateTime fin) {}

    private RangoFechas resolverRangoFechas(LocalDate startDate, LocalDate endDate, String preset) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = startDate != null ? startDate : hoy.minusDays(29);
        LocalDate fin = endDate != null ? endDate : hoy;

        if (preset != null && !preset.isBlank()) {
            String p = preset.trim().toUpperCase(Locale.ROOT);
            switch (p) {
                case "LAST_7_DAYS":
                case "ULTIMOS_7_DIAS":
                    inicio = hoy.minusDays(6);
                    fin = hoy;
                    break;
                case "LAST_30_DAYS":
                case "ULTIMOS_30_DIAS":
                    inicio = hoy.minusDays(29);
                    fin = hoy;
                    break;
                case "LAST_YEAR":
                case "ULTIMO_ANO":
                case "ULTIMO_ANHO":
                    inicio = hoy.minusYears(1);
                    fin = hoy;
                    break;
                case "THIS_YEAR":
                case "ESTE_ANO":
                case "ESTE_ANHO":
                    inicio = LocalDate.of(hoy.getYear(), 1, 1);
                    fin = hoy;
                    break;
                default:
                    break;
            }
        }

        if (inicio.isAfter(fin)) {
            LocalDate temp = inicio;
            inicio = fin;
            fin = temp;
        }

        return new RangoFechas(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));
    }

    private List<ReparacionResponseDto> filtrarReparacionesPorFecha(List<ReparacionResponseDto> reparaciones,
                                                                   RangoFechas rango) {
        return reparaciones.stream()
                .filter(r -> r.fechaCreacion() != null)
                .filter(r -> !r.fechaCreacion().isBefore(rango.inicio()) && !r.fechaCreacion().isAfter(rango.fin()))
                .toList();
    }

    private List<MovimientoStockDto> filtrarMovimientosPorFecha(List<MovimientoStockDto> movimientos,
                                                                RangoFechas rango) {
        ZoneId zone = ZoneId.systemDefault();
        return movimientos.stream()
                .filter(m -> m.fecha() != null)
                .filter(m -> {
                    LocalDateTime fechaMov = LocalDateTime.ofInstant(m.fecha(), zone);
                    return !fechaMov.isBefore(rango.inicio()) && !fechaMov.isAfter(rango.fin());
                })
                .toList();
    }

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
