package com.ObraSmart.GestionLogistica.service.impl;


import com.ObraSmart.GestionLogistica.client.EquipoClient;
import com.ObraSmart.GestionLogistica.client.ObradorClient;
import com.ObraSmart.GestionLogistica.dto.DistanciaCalculoDto;
import com.ObraSmart.GestionLogistica.dto.EquipoSimpleDto;
import com.ObraSmart.GestionLogistica.dto.ObradorSimpleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistanciaService {

    private final EquipoClient equipoClient;
    private final ObradorClient obradorClient;

    // Radio de la Tierra en kilómetros
    private static final double RADIO_TIERRA_KM = 6371.0;

    /**
     * Calcula la distancia entre un equipo y un obrador usando la fórmula de Haversine
     *
     * @param equipoId ID del equipo
     * @param obradorId ID del obrador
     * @return DTO con información de distancia y coordenadas
     */
    public DistanciaCalculoDto calcularDistancia(Long equipoId, Long obradorId) {
        log.info("Calculando distancia entre equipo {} y obrador {}", equipoId, obradorId);

        // Obtener información del equipo
        EquipoSimpleDto equipo = equipoClient.obtenerEquipo(equipoId);

        // Obtener información del obrador
        ObradorSimpleDto obrador = obradorClient.obtenerObrador(obradorId);

        // Calcular distancia usando Haversine
        double distanciaKm = calcularDistanciaHaversine(
                equipo.getLatitud(),
                equipo.getLongitud(),
                obrador.getLatitud(),
                obrador.getLongitud()
        );

        // Construir respuesta
        DistanciaCalculoDto resultado = DistanciaCalculoDto.builder()
                .equipoId(equipo.getId())
                .equipoNombre(equipo.getNombre())
                .equipoLatitud(equipo.getLatitud())
                .equipoLongitud(equipo.getLongitud())
                .obradorId(obrador.getId())
                .obradorNombre(obrador.getNombre())
                .obradorLatitud(obrador.getLatitud())
                .obradorLongitud(obrador.getLongitud())
                .distanciaKm(Math.round(distanciaKm * 100.0) / 100.0) // Redondear a 2 decimales
                .mensaje(generarMensaje(equipo.getNombre(), obrador.getNombre(), distanciaKm))
                .build();

        log.info("Distancia calculada: {} km", resultado.getDistanciaKm());
        return resultado;
    }

    /**
     * Fórmula de Haversine para calcular la distancia entre dos puntos en la esfera terrestre
     *
     * @param lat1 Latitud del punto 1 (en grados)
     * @param lon1 Longitud del punto 1 (en grados)
     * @param lat2 Latitud del punto 2 (en grados)
     * @param lon2 Longitud del punto 2 (en grados)
     * @return Distancia en kilómetros
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        // Convertir grados a radianes
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Diferencias
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Fórmula de Haversine
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia en kilómetros
        return RADIO_TIERRA_KM * c;
    }

    /**
     * Genera un mensaje descriptivo sobre la distancia
     */
    private String generarMensaje(String equipoNombre, String obradorNombre, double distanciaKm) {
        if (distanciaKm < 1) {
            return String.format("El equipo '%s' está a menos de 1 km del obrador '%s'",
                    equipoNombre, obradorNombre);
        } else if (distanciaKm < 10) {
            return String.format("El equipo '%s' está a %.2f km del obrador '%s' (distancia corta)",
                    equipoNombre, distanciaKm, obradorNombre);
        } else if (distanciaKm < 50) {
            return String.format("El equipo '%s' está a %.2f km del obrador '%s' (distancia media)",
                    equipoNombre, distanciaKm, obradorNombre);
        } else {
            return String.format("El equipo '%s' está a %.2f km del obrador '%s' (distancia larga)",
                    equipoNombre, distanciaKm, obradorNombre);
        }
    }
}