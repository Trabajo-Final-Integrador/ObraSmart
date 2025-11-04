package com.ObraSmart.GestionStock.Service.Impl;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import com.ObraSmart.GestionStock.Entity.Repuesto;
import com.ObraSmart.GestionStock.Entity.Proveedor;
import com.ObraSmart.GestionStock.Repository.RepuestoRepository;
import com.ObraSmart.GestionStock.Repository.ProveedorRepository;
import com.ObraSmart.GestionStock.Service.RepuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del RepuestoService.
 *
 * Responsabilidad:
 * - Contener la lógica de negocio de los repuestos (CRUD, control de stock, validaciones).
 * - Interactuar con los repositorios (RepuestoRepository y ProveedorRepository).
 *
 * Relaciones con otras capas:
 * - Controller -> invoca estos métodos.
 * - Repository -> persistencia en DB.
 *
 * Transaccionalidad:
 * - La clase está anotada con @Transactional para garantizar consistencia en operaciones que cambian estado.
 */
@Service
@Transactional
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;
    private final ProveedorRepository proveedorRepository;

    @Autowired
    public RepuestoServiceImpl(RepuestoRepository repuestoRepository, ProveedorRepository proveedorRepository) {
        this.repuestoRepository = repuestoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    /* ------------------- HELPERS: conversiones entidad <-> DTO ------------------- */

    /**
     * Convierte Entidad -> DTO
     * Incluye proveedorId si existe.
     */
    private RepuestoDto toDto(Repuesto repuesto) {
        RepuestoDto dto = new RepuestoDto();
        dto.setId(repuesto.getId());
        dto.setNombre(repuesto.getNombre());
        dto.setCategoria(repuesto.getCategoria());
        dto.setCantidad(repuesto.getCantidad());
        dto.setUnidad(repuesto.getUnidad());
        dto.setStockMinimo(repuesto.getStockMinimo());
        dto.setProveedorId(repuesto.getProveedor() != null ? repuesto.getProveedor().getId() : null);
        return dto;
    }

    /**
     * Convierte DTO -> Entidad (parcial, no resuelve relaciones profundas).
     * - Si dto.proveedorId está presente, hace lookup del Proveedor.
     */
    private Repuesto toEntity(RepuestoDto dto) {
        Repuesto r = new Repuesto();
        r.setId(dto.getId());
        r.setNombre(dto.getNombre());
        r.setCategoria(dto.getCategoria());
        r.setCantidad(dto.getCantidad());
        r.setUnidad(dto.getUnidad());
        r.setStockMinimo(dto.getStockMinimo());
        r.setActivo(true); // por defecto activo al crear/guardar

        if (dto.getProveedorId() != null) {
            Proveedor p = proveedorRepository.findById(dto.getProveedorId()).orElse(null);
            r.setProveedor(p);
        } else {
            r.setProveedor(null);
        }
        return r;
    }

    /* ------------------- CRUD básicos ------------------- */

    /**
     * Devuelve todos los repuestos activos.
     * Controller -> GET /api/repuestos
     */
    @Override
    public List<RepuestoDto> getAllRepuestos() {
        return repuestoRepository.findByActivoTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un repuesto activo por id.
     * Controller -> GET /api/repuestos/{id}
     */
    @Override
    public RepuestoDto getRepuestoById(Long id) {
        return repuestoRepository.findByIdAndActivoTrue(id)
                .map(this::toDto)
                .orElse(null);
    }

    /**
     * Guarda un nuevo repuesto (valida unicidad por nombre entre activos).
     * Controller -> POST /api/repuestos
     */
    @Override
    public RepuestoDto saveRepuesto(RepuestoDto dto) {
        // Validación: nombre único entre repuestos activos
        repuestoRepository.findByNombreAndActivoTrue(dto.getNombre())
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe un repuesto activo con el nombre: " + dto.getNombre());
                });

        Repuesto saved = repuestoRepository.save(toEntity(dto));
        // alerta simple: si cantidad < stockMinimo, log / aviso
        if (saved.getCantidad() < saved.getStockMinimo()) {
            // Aquí podríamos emitir un evento o crear una orden automáticamente.
            System.out.println("ALERTA: stock por debajo del mínimo para " + saved.getNombre());
        }
        return toDto(saved);
    }

    /**
     * Actualiza un repuesto activo.
     * Controller -> PUT /api/repuestos/{id}
     */
    @Override
    public RepuestoDto updateRepuesto(Long id, RepuestoDto dto) {
        return repuestoRepository.findByIdAndActivoTrue(id)
                .map(existing -> {
                    // si cambia el nombre, validar unicidad
                    if (!existing.getNombre().equals(dto.getNombre())) {
                        repuestoRepository.findByNombreAndActivoTrue(dto.getNombre())
                                .ifPresent(conflict -> {
                                    throw new RuntimeException("Ya existe otro repuesto activo con el nombre: " + dto.getNombre());
                                });
                    }

                    existing.setNombre(dto.getNombre());
                    existing.setCategoria(dto.getCategoria());
                    existing.setCantidad(dto.getCantidad());
                    existing.setUnidad(dto.getUnidad());
                    existing.setStockMinimo(dto.getStockMinimo());

                    // actualizar proveedor si viene proveedorId
                    if (dto.getProveedorId() != null) {
                        proveedorRepository.findById(dto.getProveedorId()).ifPresent(existing::setProveedor);
                    } else {
                        existing.setProveedor(null);
                    }

                    Repuesto updated = repuestoRepository.save(existing);
                    return toDto(updated);
                })
                .orElse(null);
    }

    /**
     * Baja lógica: marca activo = false.
     * Controller -> DELETE /api/repuestos/{id}
     */
    @Override
    public void deleteRepuesto(Long id) {
        repuestoRepository.findByIdAndActivoTrue(id)
                .ifPresent(repuesto -> {
                    repuesto.setActivo(false);
                    repuestoRepository.save(repuesto);
                });
    }

    /** ------------------- OPERACIONES DE STOCK ------------------- */

    /**
     * Sacar del stock: decrementa cantidad si hay stock suficiente.
     *
     * Importante: este método usa el query con PESSIMISTIC_WRITE para bloquear la fila
     * y evitar condiciones de carrera si hay múltiples consumos concurrentes.
     *
     * @param id id del repuesto
     * @param cantidad cantidad a sacar (debe ser > 0)
     * @return RepuestoDto actualizado
     * @throws RuntimeException si stock insuficiente o datos invalidos
     */
    @Override
    public RepuestoDto sacarDelStock(Long id, int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad a sacar debe ser mayor a 0");
        }

        Repuesto r = repuestoRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("Repuesto activo no encontrado con ID: " + id));

        if (r.getCantidad() >= cantidad) {
            r.setCantidad(r.getCantidad() - cantidad);
            Repuesto actualizado = repuestoRepository.save(r);

            // Si quedó por debajo del stockMinimo, registramos alerta (log aquí)
            if (actualizado.getCantidad() < actualizado.getStockMinimo()) {
                System.out.println("ALERTA: Stock bajo para " + actualizado.getNombre() +
                        ". Disponible: " + actualizado.getCantidad() +
                        " / Mínimo: " + actualizado.getStockMinimo());
                // Aquí podríamos iniciar creación automática de OrdenCompra.
            }

            return toDto(actualizado);
        } else {
            throw new RuntimeException(
                    "Stock insuficiente para '" + r.getNombre() +
                            "'. Disponible: " + r.getCantidad() +
                            ", Solicitado: " + cantidad
            );
        }
    }

    /**
     * Agregar al stock (ingreso por compra / recepción / etc).
     *
     * @param id id del repuesto
     * @param cantidad cantidad positiva a sumar
     */
    @Override
    public RepuestoDto agregarAlStock(Long id, int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad a agregar debe ser mayor a 0");
        }

        Repuesto r = repuestoRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("Repuesto activo no encontrado con ID: " + id));

        r.setCantidad(r.getCantidad() + cantidad);
        Repuesto actualizado = repuestoRepository.save(r);
        return toDto(actualizado);
    }

    /**
     * Verificar stock disponible (lectura).
     */
    @Override
    public int verificarStockDisponible(Long id) {
        return repuestoRepository.findByIdAndActivoTrue(id)
                .map(Repuesto::getCantidad)
                .orElse(0);
    }

    /**
     * Buscar repuestos cuyo stock sea menor que el stockMinimo indicado.
     * - Útil para reportes o generación automática de órdenes.
     */
    @Override
    public List<RepuestoDto> getRepuestosStockBajo(int stockMinimo) {
        return repuestoRepository.findByCantidadLessThanAndActivoTrue(stockMinimo)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** ------------------- ESTADÍSTICAS ------------------- */

    @Override
    public long contarRepuestosActivos() {
        return repuestoRepository.countByActivoTrue();
    }

    @Override
    public long contarAlertasStock(int stockMinimo) {
        return repuestoRepository.countByCantidadLessThanAndActivoTrue(stockMinimo);
    }
}
