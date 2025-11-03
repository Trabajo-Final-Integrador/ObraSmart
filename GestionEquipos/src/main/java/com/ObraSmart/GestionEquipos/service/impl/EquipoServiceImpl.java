package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.Equipo;
import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.entity.Modelo;
import com.ObraSmart.GestionEquipos.entity.TipoEquipo;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.mapers.EquipoMapers;
import com.ObraSmart.GestionEquipos.repository.EquipoRepository;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
import com.ObraSmart.GestionEquipos.repository.ModeloRepository;
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
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository,
                             TipoEquipoRepository tipoEquipoRepository,
                             MarcaRepository marcaRepository,
                             ModeloRepository modeloRepository) {
        this.equipoRepository = equipoRepository;
        this.tipoEquipoRepository = tipoEquipoRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
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

        if (dto.getNumeroPatente() != null && equipoRepository.existsByNumeroPatente(dto.getNumeroPatente())) {
            throw new BusinessException("Ya existe un equipo con la patente " + dto.getNumeroPatente(), HttpStatus.CONFLICT);
        }

        Equipo entity = EquipoMapers.toEntity(dto);

        // 🔹 Asignar relaciones
        TipoEquipo tipoEquipo = tipoEquipoRepository.findById(dto.getIdTipoEquipo())
                .orElseThrow(() -> new BusinessException("Tipo de equipo no encontrado", HttpStatus.BAD_REQUEST));
        entity.setTipoEquipo(tipoEquipo);

        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new BusinessException("Marca no encontrada", HttpStatus.BAD_REQUEST));
        entity.setMarca(marca);

        Modelo modelo = modeloRepository.findById(dto.getIdModelo())
                .orElseThrow(() -> new BusinessException("Modelo no encontrado", HttpStatus.BAD_REQUEST));
        entity.setModelo(modelo);

        Equipo saved = equipoRepository.save(entity);
        return EquipoMapers.toDTO(saved);
    }

    @Transactional
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

        // 🔹 Actualizar relaciones si es necesario
        if (!entity.getTipoEquipo().getId().equals(dto.getIdTipoEquipo())) {
            TipoEquipo tipoEquipo = tipoEquipoRepository.findById(dto.getIdTipoEquipo())
                    .orElseThrow(() -> new BusinessException("Tipo de equipo no encontrado", HttpStatus.BAD_REQUEST));
            entity.setTipoEquipo(tipoEquipo);
        }

        if (!entity.getMarca().getId().equals(dto.getIdMarca())) {
            Marca marca = marcaRepository.findById(dto.getIdMarca())
                    .orElseThrow(() -> new BusinessException("Marca no encontrada", HttpStatus.BAD_REQUEST));
            entity.setMarca(marca);
        }

        if (!entity.getModelo().getId().equals(dto.getIdModelo())) {
            Modelo modelo = modeloRepository.findById(dto.getIdModelo())
                    .orElseThrow(() -> new BusinessException("Modelo no encontrado", HttpStatus.BAD_REQUEST));
            entity.setModelo(modelo);
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
        entity.setNumeroPatente(dto.getNumeroPatente());

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
