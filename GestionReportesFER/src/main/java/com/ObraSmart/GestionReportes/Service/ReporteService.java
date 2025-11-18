package com.ObraSmart.GestionReportes.Service;



import com.ObraSmart.GestionReportes.Dto.DashboardReporteDto;

import java.util.List;

public interface ReporteService {

    /**
     * Genera el dashboard actual consumiendo otros microservicios.
     * @param guardarSi true -> persiste snapshot en la BD
     */
    DashboardReporteDto generarDashboard(boolean guardarSi);

    /**
     * Historial de snapshots guardados en BD.
     */
    List<DashboardReporteDto> obtenerHistorial(int limit);
}
