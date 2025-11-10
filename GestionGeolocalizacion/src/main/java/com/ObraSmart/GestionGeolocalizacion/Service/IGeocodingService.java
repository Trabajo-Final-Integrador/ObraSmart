package com.ObraSmart.GestionGeolocalizacion.Service;



import com.ObraSmart.GestionGeolocalizacion.Exception.GeocodingException;

/**
 * Interfaz para geolocalización de direcciones a coordenadas.
 */
public interface IGeocodingService {
    /**
     * Obtiene latitud y longitud a partir de una dirección.
     * @param direccion dirección textual
     * @return arreglo [latitud, longitud]
     * @throws GeocodingException si falla la geolocalización
     */
    double[] obtenerCoordenadas(String direccion) throws GeocodingException;
}
