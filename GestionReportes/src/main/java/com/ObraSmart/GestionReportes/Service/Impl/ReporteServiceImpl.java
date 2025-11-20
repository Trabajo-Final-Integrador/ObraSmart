package com.ObraSmart.GestionReportes.Service.Impl;

import lombok.RequiredArgsConstructor;
import com.ObraSmart.GestionReportes.Dto.DashboardReporteDto;
import com.ObraSmart.GestionReportes.Dto.Externos.EquipoDTO;
import com.ObraSmart.GestionReportes.Dto.Externos.MovimientoStockDto;
import com.ObraSmart.GestionReportes.Dto.Externos.ReparacionResponseDto;
import com.ObraSmart.GestionReportes.Dto.Externos.RepuestoDto;
import com.ObraSmart.GestionReportes.Entity.DashboardSnapshot;
import com.ObraSmart.GestionReportes.Repository.DashboardSnapshotRepository;
import com.ObraSmart.GestionReportes.Service.ReporteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final RestTemplate restTemplate;
    private final DashboardSnapshotRepository dashboardSnapshotRepository;

    @Value("${obrasmart.reparaciones.base-url}")
    private String reparacionesBaseUrl;

    @Value("${obrasmart.stock.repuestos-url}")
    private String repuestosUrl;

    @Value("${obrasmart.stock.movimientos-url}")
    private String movimientosUrl;

    @Value("${obrasmart.equipos.base-url}")
    private String equiposBaseUrl;

    @Override
    public DashboardReporteDto generarDashboard(boolean guardarSi) {

        // 1) Reparaciones
        ReparacionResponseDto[] reparacionesArray =
                restTemplate.getForObject(reparacionesBaseUrl, ReparacionResponseDto[].class);
        List<ReparacionResponseDto> reparaciones =
                reparacionesArray != null ? Arrays.asList(reparacionesArray) : List.of();

        long totalReparaciones = reparaciones.size();
        long totalReparacionesFinalizadas = reparaciones.stream()
                .filter(r -> "FINALIZADA".equalsIgnoreCase(r.estado()))
                .count();
        long totalReparacionesAbiertas = totalReparaciones - totalReparacionesFinalizadas;

        // 2) Equipos
        EquipoDTO[] equiposArray =
                restTemplate.getForObject(equiposBaseUrl, EquipoDTO[].class);
        long totalEquipos = equiposArray != null ? equiposArray.length : 0L;

        // 3) Repuestos
        RepuestoDto[] repuestosArray =
                restTemplate.getForObject(repuestosUrl, RepuestoDto[].class);
        List<RepuestoDto> repuestos =
                repuestosArray != null ? Arrays.asList(repuestosArray) : List.of();

        long totalRepuestos = repuestos.size();
        long repuestosCriticos = repuestos.stream()
                .filter(r -> r.stock() != null && r.stockMinimo() != null
                        && r.stock() <= r.stockMinimo())
                .count();

        // 4) Movimientos
        MovimientoStockDto[] movimientosArray =
                restTemplate.getForObject(movimientosUrl, MovimientoStockDto[].class);
        long totalMovimientos = movimientosArray != null ? movimientosArray.length : 0L;

        LocalDateTime ahora = LocalDateTime.now();

        DashboardSnapshot snapshot = DashboardSnapshot.builder()
                .fechaGeneracion(ahora)
                .totalReparaciones(totalReparaciones)
                .totalReparacionesAbiertas(totalReparacionesAbiertas)
                .totalReparacionesFinalizadas(totalReparacionesFinalizadas)
                .totalEquipos(totalEquipos)
                .totalRepuestos(totalRepuestos)
                .totalMovimientos(totalMovimientos)
                .repuestosCriticos(repuestosCriticos)
                .build();

        if (guardarSi) {
            snapshot = dashboardSnapshotRepository.save(snapshot);
        }

        return DashboardReporteDto.builder()
                .id(snapshot.getId())
                .fechaGeneracion(snapshot.getFechaGeneracion())
                .totalReparaciones(snapshot.getTotalReparaciones())
                .totalReparacionesAbiertas(snapshot.getTotalReparacionesAbiertas())
                .totalReparacionesFinalizadas(snapshot.getTotalReparacionesFinalizadas())
                .totalEquipos(snapshot.getTotalEquipos())
                .totalRepuestos(snapshot.getTotalRepuestos())
                .totalMovimientos(snapshot.getTotalMovimientos())
                .repuestosCriticos(snapshot.getRepuestosCriticos())
                .build();
    }

    @Override
    public List<DashboardReporteDto> obtenerHistorial(int limit) {
        List<DashboardSnapshot> snapshots = dashboardSnapshotRepository
                .findTop10ByOrderByFechaGeneracionDesc();

        return snapshots.stream()
                .limit(limit > 0 ? limit : 10)
                .map(s -> DashboardReporteDto.builder()
                        .id(s.getId())
                        .fechaGeneracion(s.getFechaGeneracion())
                        .totalReparaciones(s.getTotalReparaciones())
                        .totalReparacionesAbiertas(s.getTotalReparacionesAbiertas())
                        .totalReparacionesFinalizadas(s.getTotalReparacionesFinalizadas())
                        .totalEquipos(s.getTotalEquipos())
                        .totalRepuestos(s.getTotalRepuestos())
                        .totalMovimientos(s.getTotalMovimientos())
                        .repuestosCriticos(s.getRepuestosCriticos())
                        .build())
                .collect(Collectors.toList());
    }
}
