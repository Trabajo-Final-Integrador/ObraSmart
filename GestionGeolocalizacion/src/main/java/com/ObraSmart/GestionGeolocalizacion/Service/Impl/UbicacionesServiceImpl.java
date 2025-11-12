package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionDTO;
import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionResponse;
import com.ObraSmart.GestionGeolocalizacion.Dto.ReparacionLiteDTO;
import com.ObraSmart.GestionGeolocalizacion.Exception.GeocodingException;
import com.ObraSmart.GestionGeolocalizacion.Service.IEquiposClientService;
import com.ObraSmart.GestionGeolocalizacion.Service.IReparacionesClientService;
import com.ObraSmart.GestionGeolocalizacion.Service.IGeocodingService;
import com.ObraSmart.GestionGeolocalizacion.Service.IUbicacionesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UbicacionesServiceImpl implements IUbicacionesService {

    private final IEquiposClientService equiposClient;
    private final IReparacionesClientService reparacionesClient;
    private final IGeocodingService geocoding;

    public UbicacionesServiceImpl(IEquiposClientService equiposClient,
                                  IReparacionesClientService reparacionesClient,
                                  IGeocodingService geocoding) {
        this.equiposClient = equiposClient;
        this.reparacionesClient = reparacionesClient;
        this.geocoding = geocoding;
    }

    @Override
    public List<EquipoUbicacionResponse> listarParaMapa() {
        List<EquipoUbicacionDTO> equipos = equiposClient.obtenerTodos();
        List<ReparacionLiteDTO> reparaciones = reparacionesClient.obtenerTodas();

        // 🔹 Identificar equipos que están en reparación
        Set<Long> equiposEnReparacion = reparaciones.stream()
                .filter(r -> r.getEstado() != null && r.getEstado().toLowerCase().contains("progreso"))
                .map(ReparacionLiteDTO::getEquipoId)
                .collect(Collectors.toSet());

        System.out.println("✅ Total de equipos recibidos: " + equipos.size());

        return equipos.stream().map(e -> {
            double lat = 0.0;
            double lon = 0.0;

            // ✅ PRIORIDAD 1: usar coordenadas reales si existen
            if (e.getLatitud() != null && e.getLongitud() != null) {
                lat = e.getLatitud();
                lon = e.getLongitud();
                System.out.println("📍 Coordenadas desde BD para " + e.getNombre() + ": " + lat + ", " + lon);
            }
            // ✅ PRIORIDAD 2: intentar geocodificar si no hay lat/lon
            else if (e.getUbicacionActual() != null && !e.getUbicacionActual().isBlank()) {
                try {
                    String direccion = e.getUbicacionActual() + ", Córdoba, Argentina";
                    double[] coords = geocoding.obtenerCoordenadas(direccion);
                    lat = coords[0];
                    lon = coords[1];
                    System.out.println("🌍 Geocodificado " + e.getNombre() + " → " + lat + ", " + lon);
                } catch (GeocodingException ex) {
                    System.out.println("⚠️ Error geocodificando " + e.getUbicacionActual() + ": " + ex.getMessage());
                }
            } else {
                System.out.println("⚠️ Equipo sin ubicación ni coordenadas: " + e.getNombre());
            }

            String estadoFinal = equiposEnReparacion.contains(e.getId())
                    ? "EN_REPARACION"
                    : (e.getEstado() == null ? "DESCONOCIDO" : e.getEstado());

            return new EquipoUbicacionResponse(
                    e.getId(),
                    e.getNombre(),
                    lat,
                    lon,
                    estadoFinal
            );
        }).toList();
    }
}
