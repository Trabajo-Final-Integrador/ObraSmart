package com.ObraSmart.GestionGeolocalizacion.Exception;

/**
 * Excepción para errores de geocoding.
 */
public class GeocodingException extends Exception {

    public GeocodingException() { super(); }
    public GeocodingException(String message) { super(message); }
    public GeocodingException(String message, Throwable cause) { super(message, cause); }
    public GeocodingException(Throwable cause) { super(cause); }
}
