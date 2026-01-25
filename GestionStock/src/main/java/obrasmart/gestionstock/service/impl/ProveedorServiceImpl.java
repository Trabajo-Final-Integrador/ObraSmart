package obrasmart.gestionstock.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import obrasmart.gestionstock.dto.ProveedorCreateDTO;
import obrasmart.gestionstock.dto.ProveedorDto;
import obrasmart.gestionstock.dto.ProveedorListadoDTO;
import obrasmart.gestionstock.dto.ProveedorUpdateDTO;
import obrasmart.gestionstock.entity.proveedores.EstadoProveedor;
import obrasmart.gestionstock.entity.proveedores.Proveedor;
import obrasmart.gestionstock.exception.ResourceNotFoundException;
import obrasmart.gestionstock.mapper.ProveedorMapper;
import obrasmart.gestionstock.repository.ProveedorRepository;
import obrasmart.gestionstock.service.ProveedorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service @RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repo;
    private final ProveedorMapper mapper;



    @Override
    public ProveedorDto crear(ProveedorCreateDTO dto) {

        
        log.info("📦 Intentando guardar proveedor: {}", dto);
        try {
            if (repo.existsByCuit(dto.getCuit())) {
                throw new IllegalArgumentException("Ya existe un proveedor con el CUIT: " + dto.getCuit());
            }
            Proveedor entity = mapper.toEntity(dto);
            Proveedor saved = repo.save(entity);
            log.info("✅ Proveedor guardado con id {}", saved.getId());
            return mapper.toDTO(saved);
        } catch (Exception e) {
            log.error("💥 Error en crear proveedor", e);
            throw e;
        }
    }

    @Override
    public ProveedorDto actualizar(Long id, ProveedorUpdateDTO dto) {
        log.info("🔄 Actualizando proveedor ID {}", id);

        Proveedor proveedor = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        // Validar CUIT si cambia
        if (dto.getCuit() != null && !dto.getCuit().equals(proveedor.getCuit())) {
            if (repo.existsByCuit(dto.getCuit())) {
                throw new IllegalArgumentException("Ya existe un proveedor con el CUIT: " + dto.getCuit());
            }
        }

        // ✔ Evitar que se pisen campos NOT NULL con null o vacío
        validarNoBorrarCamposObligatorios(dto, proveedor);

        mapper.updateEntityFromDTO(dto, proveedor);

        Proveedor updatedProveedor = repo.save(proveedor);
        log.info("✅ Proveedor actualizado correctamente: {}", updatedProveedor.getId());

        return mapper.toDTO(updatedProveedor);
    }

    private void validarNoBorrarCamposObligatorios(ProveedorUpdateDTO dto, Proveedor proveedor) {

        if (dto.getTelefono() != null && dto.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede quedar vacío.");
        }
        if (dto.getDireccion() != null && dto.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La dirección no puede quedar vacía.");
        }
        if (dto.getCiudad() != null && dto.getCiudad().isBlank()) {
            throw new IllegalArgumentException("La ciudad no puede quedar vacía.");
        }
        if (dto.getProvincia() != null && dto.getProvincia().isBlank()) {
            throw new IllegalArgumentException("La provincia no puede quedar vacía.");
        }
        if (dto.getCondicionIVA() == null && proveedor.getCondicionIVA() == null) {
            throw new IllegalArgumentException("La condición IVA es obligatoria.");
        }
    }

    @Override
    @Transactional
    public ProveedorDto buscar(Long id) {

        Proveedor proveedor = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        return mapper.toDTO(proveedor);
    }


    @Transactional
    @Override
    public List<ProveedorListadoDTO> listarLite() {
        return repo.findAll().stream()
                .map(p -> new ProveedorListadoDTO(
                        p.getId(),
                        p.getRazonSocial(),
                        p.getEspecialidad(),
                        p.getTelefono() != null ? p.getTelefono() : p.getEmail(),
                        p.getEstado()
                ))
                .toList();
    }



    @Override
    @Transactional
    public List<ProveedorDto> getByEstado(String estado) {

        EstadoProveedor estadoEnum = EstadoProveedor.valueOf(estado.toUpperCase());

        return repo.findByEstado(estadoEnum).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProveedorDto> search(String search) {

        return repo.searchProveedores(search).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProveedorDto> searchByEstado(String estado, String search) {

        EstadoProveedor estadoEnum = EstadoProveedor.valueOf(estado.toUpperCase());

        return repo.searchByEstadoAndText(estadoEnum, search).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {

        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor no encontrado con ID: " + id);
        }

        repo.deleteById(id);

    }

    @Override
    public void cambiarEstado(Long id, String nuevoEstado) {


        Proveedor proveedor = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        EstadoProveedor estadoEnum = EstadoProveedor.valueOf(nuevoEstado.toUpperCase());
        proveedor.setEstado(estadoEnum);
        repo.save(proveedor);

    }


    @Override
    @Transactional
    public boolean existsByCuit(String cuit) {
        return repo.existsByCuit(cuit);
    }
}