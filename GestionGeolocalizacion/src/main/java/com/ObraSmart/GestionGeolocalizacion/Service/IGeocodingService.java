package com.ObraSmart.GestionGeolocalizacion.Service;

public interface IGeocodingService {
    /**
     * Obtiene las coordenadas (latitud y longitud) para una dirección.
     * @param direccion dirección física a geolocalizar.
     * @return arreglo [latitud, longitud].
     */
    double[] obtenerCoordenadas(String direccion);
}
