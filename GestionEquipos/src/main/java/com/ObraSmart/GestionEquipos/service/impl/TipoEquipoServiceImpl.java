package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.entity.TipoEquipo;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import com.ObraSmart.GestionEquipos.service.TipoEquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEquipoServiceImpl implements TipoEquipoService {

    private final TipoEquipoRepository tipoEquipoRepository;

    @Override
    public TipoEquipoDTO create(TipoEquipoDTO dto) {

        if (dto.getPrefijo() == null || dto.getPrefijo().isBlank()) {
            throw new BusinessException("El prefijo no puede estar vacío", HttpStatus.BAD_REQUEST);
        }

        dto.setPrefijo(dto.getPrefijo().trim().toUpperCase());

        if (!dto.getPrefijo().matches("^[A-Z]{1,4}$")) {
            throw new BusinessException(
                    "El prefijo debe contener entre 1 y 4 letras mayúsculas",
                    HttpStatus.BAD_REQUEST
            );
        }

        TipoEquipo entity = new TipoEquipo();
        entity.setNombre(dto.getNombre());
        entity.setPrefijo(dto.getPrefijo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setImagenURL(dto.getImagenURL());

        TipoEquipo saved = tipoEquipoRepository.save(entity);

        return new TipoEquipoDTO(
                saved.getId(),
                saved.getNombre(),
                saved.getPrefijo(),
                saved.getDescripcion(),
                saved.getImagenURL()
        );
    }

    @Override
    public TipoEquipoDTO update(Long id, TipoEquipoDTO dto) {

        TipoEquipo entity = tipoEquipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo equipo no encontrado"));

        if (dto.getPrefijo() != null && !dto.getPrefijo().isBlank()) {
            dto.setPrefijo(dto.getPrefijo().trim().toUpperCase());

            if (!dto.getPrefijo().matches("^[A-Z]{1,4}$")) {
                throw new BusinessException("Prefijo inválido", HttpStatus.BAD_REQUEST);
            }

            entity.setPrefijo(dto.getPrefijo());
        }

        if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) entity.setDescripcion(dto.getDescripcion());
        if (dto.getImagenURL() != null) entity.setImagenURL(dto.getImagenURL());

        TipoEquipo updated = tipoEquipoRepository.save(entity);

        return new TipoEquipoDTO(
                updated.getId(),
                updated.getNombre(),
                updated.getPrefijo(),
                updated.getDescripcion(),
                updated.getImagenURL()
        );
    }

    @Override
    public void delete(Long id) {
        TipoEquipo entity = tipoEquipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo equipo no encontrado"));

        tipoEquipoRepository.delete(entity);
    }

    @Override
    public TipoEquipoDTO getById(Long id) {
        TipoEquipo entity = tipoEquipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo equipo no encontrado"));

        return new TipoEquipoDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getPrefijo(),
                entity.getDescripcion(),
                entity.getImagenURL()
        );
    }

    @Override
    public List<TipoEquipoDTO> getAll() {
        return tipoEquipoRepository.findAll()
                .stream()
                .map(t -> new TipoEquipoDTO(
                        t.getId(),
                        t.getNombre(),
                        t.getPrefijo(),
                        t.getDescripcion(),
                        t.getImagenURL()
                ))
                .toList();
    }
}
