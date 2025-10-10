package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.Equipo;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.mapers.EquipoMapers;
import com.ObraSmart.GestionEquipos.repository.EquipoRepository;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import com.ObraSmart.GestionEquipos.service.EquipoService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final TipoEquipoRepository tipoEquipoRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository, TipoEquipoRepository tipoEquipoRepository) {
        this.equipoRepository = equipoRepository;
        this.tipoEquipoRepository = tipoEquipoRepository;

    }


    @Override
    public List<EquipoDTO> getAll() {
        return equipoRepository.findAll()
                .stream()
                .map(EquipoMapers::toDTO)
                .toList();
    }

    @Override
    public EquipoDTO getById(Long id) {
        Equipo entity = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con ID " + id));
        return EquipoMapers.toDTO(entity);
    }

    @Transactional
    @Override
    public EquipoDTO create(EquipoDTO dto) {
        if (equipoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new BusinessException("Ya existe un equipo con el número de serie " + dto.getNumeroSerie(), HttpStatus.CONFLICT);
        }

        // 🔹 Validación de patente duplicada (si tiene)
        if (dto.getNumeroPatente() != null && equipoRepository.existsByNumeroPatente(dto.getNumeroPatente())) {
            throw new BusinessException("Ya existe un equipo con la patente " + dto.getNumeroPatente(),HttpStatus.CONFLICT);
        }

        Equipo entity = EquipoMapers.toEntity(dto);
        Equipo saved = equipoRepository.save(entity);
        return EquipoMapers.toDTO(saved);
    }

    @Override
    public EquipoDTO update(Long id, EquipoDTO dto) {
        Equipo entity = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con ID " + id));

        if (!entity.getNumeroSerie().equals(dto.getNumeroSerie()) &&
                equipoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new BusinessException("El número de serie ya pertenece a otro equipo.", HttpStatus.CONFLICT);
        }


        if (dto.getNumeroPatente() != null &&
                !dto.getNumeroPatente().equals(entity.getNumeroPatente()) &&
                equipoRepository.existsByNumeroPatente(dto.getNumeroPatente())) {
            throw new BusinessException("La patente ya pertenece a otro equipo.", HttpStatus.CONFLICT);
        }

        entity.setEstadoOperativo(dto.getEstadoOperativo());
        entity.setKilometrajeHorasUso(dto.getKilometrajeHorasUso());
        entity.setFechaUltimoMantenimiento(dto.getFechaUltimoMantenimiento());
        entity.setProximoMantenimiento(dto.getProximoMantenimiento());
        entity.setResponsableMantenimiento(dto.getResponsableMantenimiento());
        entity.setSeguroVigente(dto.getSeguroVigente());
        entity.setFechaVencimientoSeguro(dto.getFechaVencimientoSeguro());
        entity.setUbicacionActual(dto.getUbicacionActual());
        entity.setActivo(dto.getActivo());

        Equipo updated = equipoRepository.save(entity);
        return EquipoMapers.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Equipo entity = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con ID " + id));

        entity.setActivo(false);
        equipoRepository.save(entity);
    }
}
