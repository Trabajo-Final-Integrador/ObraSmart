package com.ObraSmart.GestionStock.Service;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import java.util.List;

/**
 * Interfaz para el servicio de gestión de repuestos.
 * Define los métodos que debe implementar el servicio.
 */
public interface RepuestoService {

    /**
     * Obtiene todos los repuestos convertidos a DTO
     * @return Lista de RepuestoDto
     */
    List<RepuestoDto> getAllRepuestos();

    /**
     * Obtiene un repuesto por su ID convertido a DTO
     * @param id ID del repuesto
     * @return RepuestoDto o null si no existe
     */
    RepuestoDto getRepuestoById(Long id);

    /**
     * Guarda un nuevo repuesto
     * @param dto DTO con los datos del repuesto
     * @return RepuestoDto guardado
     */
    RepuestoDto saveRepuesto(RepuestoDto dto);

    /**
     * Actualiza un repuesto existente
     * @param id ID del repuesto a actualizar
     * @param dto DTO con los nuevos datos
     * @return RepuestoDto actualizado o null si no existe
     */
    RepuestoDto updateRepuesto(Long id, RepuestoDto dto);

    /**
     * Elimina un repuesto por su ID (baja lógica)
     * @param id ID del repuesto a eliminar
     */
    void deleteRepuesto(Long id);

    // 🔹 NUEVOS MÉTODOS PARA GESTIÓN DE STOCK

    /**
     * Sacar una cantidad específica del stock
     * @param id ID del repuesto
     * @param cantidad Cantidad a sacar del stock
     * @return RepuestoDto actualizado
     */
    RepuestoDto sacarDelStock(Long id, int cantidad);

    /**
     * Agregar una cantidad específica al stock
     * @param id ID del repuesto
     * @param cantidad Cantidad a agregar al stock
     * @return RepuestoDto actualizado
     */
    RepuestoDto agregarAlStock(Long id, int cantidad);

    /**
     * Verificar stock disponible de un repuesto
     * @param id ID del repuesto
     * @return Cantidad disponible en stock
     */
    int verificarStockDisponible(Long id);

    /**
     * Obtener repuestos con stock bajo
     * @param stockMinimo Límite mínimo para alerta
     * @return Lista de repuestos con stock bajo
     */
    List<RepuestoDto> getRepuestosStockBajo(int stockMinimo);
}