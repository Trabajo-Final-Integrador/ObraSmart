package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionDTO;
import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionResponse;
import com.ObraSmart.GestionGeolocalizacion.Dto.ReparacionLiteDTO;
import com.ObraSmart.GestionGeolocalizacion.Exception.GeocodingException;
import com.ObraSmart.GestionGeolocalizacion.Service.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        try {
       List<EquipoUbicacionDTO> equipos = equiposClient.obtenerTodos();
        List<ReparacionLiteDTO> reparaciones = reparacionesClient.obtenerTodas();

        Set<Long> equiposEnReparacion = reparaciones.stream()
                .filter(r -> r.getEstado() != null && r.getEstado().toLowerCase().contains("progreso"))
                .map(ReparacionLiteDTO::getEquipoId)
                .collect(Collectors.toSet());

        // 🧪 BLOQUE DE PRUEBA MANUAL
        /*System.out.println("📍 Probando dirección manual: Av. Corrientes 123, Buenos Aires");
        try {
            double[] testCoords = geocoding.obtenerCoordenadas("Av. Corrientes 123, Buenos Aires, Argentina");
            System.out.println("✅ Resultado test => lat=" + testCoords[0] + ", lon=" + testCoords[1]);
        } catch (Exception e) {
            System.out.println("❌ Error al obtener coordenadas de prueba:");
            e.printStackTrace();
        }*/
        // 🧪 FIN BLOQUE DE PRUEBA

        return equipos.stream().map(e -> {
            double[] coords = {0.0, 0.0};
            try {
                if (e.getUbicacionActual() != null && !e.getUbicacionActual().isBlank()) {
                    coords = geocoding.obtenerCoordenadas(e.getUbicacionActual());
                }
            } catch (GeocodingException ignored) {}

            String estadoFinal = equiposEnReparacion.contains(e.getId())
                    ? "EN_REPARACION"
                    : (e.getEstado() == null ? "DESCONOCIDO" : e.getEstado());

            return new EquipoUbicacionResponse(
                    e.getId(),
                    e.getNombre(),
                    coords[0],
                    coords[1],
                    estadoFinal
            );
        }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error interno en listarParaMapa: " + e.getMessage());
        }

    }
}
