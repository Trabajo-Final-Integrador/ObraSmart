package com.ObraSmart.GestionStock.Service;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import java.util.List;

/**
 * Interface de servicio para Repuestos.
 * Define las operaciones de negocio que ofrece la capa Service.
 */
public interface RepuestoService {
    List<RepuestoDto> getAllRepuestos();
    RepuestoDto getRepuestoById(Long id);
    RepuestoDto saveRepuesto(RepuestoDto dto);
    RepuestoDto updateRepuesto(Long id, RepuestoDto dto);
    void deleteRepuesto(Long id);

    // operaciones de stock
    RepuestoDto sacarDelStock(Long id, int cantidad);
    RepuestoDto agregarAlStock(Long id, int cantidad);
    int verificarStockDisponible(Long id);
    List<RepuestoDto> getRepuestosStockBajo(int stockMinimo);

    // estadisticas
    long contarRepuestosActivos();
    long contarAlertasStock(int stockMinimo);
}
