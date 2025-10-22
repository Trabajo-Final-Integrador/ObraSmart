package com.ObraSmart.GestionStock.Service.Impl;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import com.ObraSmart.GestionStock.Entity.Repuesto;
import com.ObraSmart.GestionStock.Repository.RepuestoRepository;
import com.ObraSmart.GestionStock.Service.RepuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;

    @Autowired
    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    // 🔹 Convertir entidad a DTO
    private RepuestoDto toDto(Repuesto repuesto) {
        RepuestoDto dto = new RepuestoDto();
        dto.setId(repuesto.getId());
        dto.setNombre(repuesto.getNombre());
        dto.setCategoria(repuesto.getCategoria());
        dto.setCantidad(repuesto.getCantidad());
        dto.setUnidad(repuesto.getUnidad());
        return dto;
    }

    // 🔹 Convertir DTO a entidad
    private Repuesto toEntity(RepuestoDto dto) {
        Repuesto repuesto = new Repuesto();
        repuesto.setId(dto.getId());
        repuesto.setNombre(dto.getNombre());
        repuesto.setCategoria(dto.getCategoria());
        repuesto.setCantidad(dto.getCantidad());
        repuesto.setUnidad(dto.getUnidad());
        repuesto.setActivo(true); // ← NUEVO: Siempre activo al crear
        return repuesto;
    }

    // 🔹 MÉTODOS USANDO BAJAS LÓGICAS

    @Override
    public List<RepuestoDto> getAllRepuestos() {
        return repuestoRepository.findByActivoTrue() // ← SOLO ACTIVOS
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RepuestoDto getRepuestoById(Long id) {
        return repuestoRepository.findByIdAndActivoTrue(id) // ← SOLO SI ESTÁ ACTIVO
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public RepuestoDto saveRepuesto(RepuestoDto dto) {
        // Validar que no exista un repuesto activo con el mismo nombre
        repuestoRepository.findByNombreAndActivoTrue(dto.getNombre())
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe un repuesto activo con el nombre: " + dto.getNombre());
                });

        Repuesto saved = repuestoRepository.save(toEntity(dto));
        return toDto(saved);
    }

    @Override
    public RepuestoDto updateRepuesto(Long id, RepuestoDto dto) {
        return repuestoRepository.findByIdAndActivoTrue(id) // ← SOLO SI ESTÁ ACTIVO
                .map(existing -> {
                    // Validar nombre único (excluyendo el actual)
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
                    Repuesto updated = repuestoRepository.save(existing);
                    return toDto(updated);
                })
                .orElse(null);
    }

    @Override
    public void deleteRepuesto(Long id) {
        repuestoRepository.findByIdAndActivoTrue(id) // ← SOLO SI ESTÁ ACTIVO
                .ifPresent(repuesto -> {
                    repuesto.setActivo(false); // ← BAJA LÓGICA
                    repuestoRepository.save(repuesto);
                });
    }

    // 🔹 MÉTODOS DE GESTIÓN DE STOCK

    @Override
    public RepuestoDto sacarDelStock(Long id, int cantidad) {
        return repuestoRepository.findByIdAndActivoTrue(id) // ← SOLO SI ESTÁ ACTIVO
                .map(repuesto -> {
                    if (cantidad <= 0) {
                        throw new RuntimeException("La cantidad a sacar debe ser mayor a 0");
                    }

                    if (repuesto.getCantidad() >= cantidad) {
                        repuesto.setCantidad(repuesto.getCantidad() - cantidad);
                        Repuesto actualizado = repuestoRepository.save(repuesto);
                        return toDto(actualizado);
                    } else {
                        throw new RuntimeException(
                                "Stock insuficiente para '" + repuesto.getNombre() +
                                        "'. Disponible: " + repuesto.getCantidad() +
                                        ", Solicitado: " + cantidad
                        );
                    }
                })
                .orElseThrow(() -> new RuntimeException("Repuesto activo no encontrado con ID: " + id));
    }

    @Override
    public RepuestoDto agregarAlStock(Long id, int cantidad) {
        return repuestoRepository.findByIdAndActivoTrue(id) // ← SOLO SI ESTÁ ACTIVO
                .map(repuesto -> {
                    if (cantidad <= 0) {
                        throw new RuntimeException("La cantidad a agregar debe ser mayor a 0");
                    }

                    repuesto.setCantidad(repuesto.getCantidad() + cantidad);
                    Repuesto actualizado = repuestoRepository.save(repuesto);
                    return toDto(actualizado);
                })
                .orElseThrow(() -> new RuntimeException("Repuesto activo no encontrado con ID: " + id));
    }

    @Override
    public int verificarStockDisponible(Long id) {
        return repuestoRepository.findByIdAndActivoTrue(id) // ← SOLO SI ESTÁ ACTIVO
                .map(Repuesto::getCantidad)
                .orElseThrow(() -> new RuntimeException("Repuesto activo no encontrado con ID: " + id));
    }

    @Override
    public List<RepuestoDto> getRepuestosStockBajo(int stockMinimo) {
        return repuestoRepository.findByCantidadLessThanAndActivoTrue(stockMinimo) // ← SOLO ACTIVOS
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 🔹 MÉTODO ADICIONAL PARA ESTADÍSTICAS
    public long contarRepuestosActivos() {
        return repuestoRepository.countByActivoTrue();
    }

    public long contarAlertasStock(int stockMinimo) {
        return repuestoRepository.countByCantidadLessThanAndActivoTrue(stockMinimo);
    }
}


