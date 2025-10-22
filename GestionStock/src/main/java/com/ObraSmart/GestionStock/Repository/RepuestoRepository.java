package com.ObraSmart.GestionStock.Repository;



import com.ObraSmart.GestionStock.Entity.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que se comunica directamente con la base de datos.
 * Extiende JpaRepository, por lo que hereda métodos como:
 * findAll(), findById(), save(), deleteById().
 * Se usa desde el servicio para obtener o guardar datos.
 */

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {

    // 🔹 NUEVOS MÉTODOS PARA BAJAS LÓGICAS Y CONSULTAS ESPECÍFICAS

    /**
     * Buscar solo repuestos activos (no eliminados lógicamente)
     * @return Lista de repuestos activos
     */
    List<Repuesto> findByActivoTrue();

    /**
     * Buscar repuesto por ID solo si está activo
     * @param id ID del repuesto
     * @return Repuesto activo o empty
     */
    Optional<Repuesto> findByIdAndActivoTrue(Long id);

    /**
     * Buscar repuestos con stock bajo y activos
     * @param stockMinimo Límite de stock mínimo
     * @return Lista de repuestos con stock bajo
     */
    List<Repuesto> findByCantidadLessThanAndActivoTrue(int stockMinimo);

    /**
     * Buscar repuesto por nombre y activo
     * @param nombre Nombre del repuesto
     * @return Repuesto activo o empty
     */
    Optional<Repuesto> findByNombreAndActivoTrue(String nombre);

    /**
     * Contar repuestos activos
     * @return Número de repuestos activos
     */
    long countByActivoTrue();

    /**
     * Contar repuestos con stock bajo y activos
     * @param stockMinimo Límite de stock mínimo
     * @return Número de repuestos con stock bajo
     */
    long countByCantidadLessThanAndActivoTrue(int stockMinimo);
}


/**
                      ✅ Metodos Usados :
findByActivoTrue() - Para consultar solo repuestos activos
findByIdAndActivoTrue() - Para buscar por ID solo si está activo
findByCantidadLessThanAndActivoTrue() - Para alertas de stock bajo
Métodos de conteo - Para estadísticas y dashboards
Búsqueda por nombre - Para validaciones de duplicados
*/