package com.ObraSmart.GestionStock.Controller;

import com.ObraSmart.GestionStock.Dto.ProveedorDto;
import com.ObraSmart.GestionStock.Entity.Proveedor;
import com.ObraSmart.GestionStock.Repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  para administrar Proveedores usando DTOs.
 */
@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;

    // Obtener todos los proveedores
    @GetMapping
    public ResponseEntity<List<ProveedorDto>> getAll() {
        List<ProveedorDto> dtos = proveedorRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Obtener un proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDto> getById(@PathVariable Long id) {
        return proveedorRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un proveedor
    @PostMapping
    public ResponseEntity<ProveedorDto> create(@RequestBody ProveedorDto dto) {
        Proveedor proveedor = fromDto(dto);
        Proveedor saved = proveedorRepository.save(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    // Actualizar un proveedor
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDto> update(@PathVariable Long id, @RequestBody ProveedorDto dto) {
        return proveedorRepository.findById(id)
                .map(existing -> {
                    // Actualizamos solo los campos del DTO
                    existing.setNombre(dto.getNombre());
                    existing.setContacto(dto.getContacto());
                    existing.setTelefono(dto.getTelefono());
                    existing.setEmail(dto.getEmail());
                    existing.setDireccion(dto.getDireccion());
                    existing.setIdentificador(dto.getIdentificador());

                    Proveedor updated = proveedorRepository.save(existing);
                    return ResponseEntity.ok(toDto(updated));
                }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ----- Métodos auxiliares para conversión entre DTO y entidad -----

    private ProveedorDto toDto(Proveedor proveedor) {
        return ProveedorDto.builder()
                .id(proveedor.getId())
                .nombre(proveedor.getNombre())
                .contacto(proveedor.getContacto())
                .telefono(proveedor.getTelefono())
                .email(proveedor.getEmail())
                .direccion(proveedor.getDireccion())
                .build();
    }

    private Proveedor fromDto(ProveedorDto dto) {
        return Proveedor.builder()
                .nombre(dto.getNombre())
                .contacto(dto.getContacto())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .identificador(dto.getIdentificador())
                .build();
    }
}
