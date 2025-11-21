package com.ObraSmart.GestionReparaciones.Client;

public interface EquipoClient {

    /**
     * Verifica que el equipo exista en el micro de Equipos.
     * Si no existe, lanza excepción.
     */
    void validarEquipoExiste(Long equipoId);

    /**
     * Actualiza el estado operativo del equipo (DISPONIBLE, EN_MANTENIMIENTO, etc).
     * TODO: requerirá que el micro de Equipos tenga un endpoint para esto.
     */
    void actualizarEstadoEquipo(Long equipoId, String nuevoEstado);
}
