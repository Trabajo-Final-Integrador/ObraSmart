package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.*;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.mapers.EquipoMapers;
import com.ObraSmart.GestionEquipos.repository.EquipoRepository;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
import com.ObraSmart.GestionEquipos.repository.ModeloRepository;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import com.ObraSmart.GestionEquipos.service.EquipoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final TipoEquipoRepository tipoRepo;
    private final MarcaRepository marcaRepo;
    private final ModeloRepository modeloRepo;

    @Override
    public List<EquipoDTO> getAll() {
        return equipoRepository.findAll()
                .stream()
                .map(EquipoMapers::toDTO)
                .toList();
    }

    @Override
    public EquipoDTO getById(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con id " + id));
        return EquipoMapers.toDTO(equipo);
    }

    @Override
    public EquipoDTO create(EquipoDTO dto) {

        // Validar número de serie duplicado
        if (equipoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new BusinessException(
                    "Ya existe un equipo con número de serie " + dto.getNumeroSerie(),
                    HttpStatus.CONFLICT
            );
        }

        // Validar código interno duplicado SOLO si viene desde el front
        if (dto.getCodigoInterno() != null && !dto.getCodigoInterno().isBlank()
                && equipoRepository.existsByCodigoInterno(dto.getCodigoInterno())) {
            throw new BusinessException(
                    "Ya existe un equipo con código interno " + dto.getCodigoInterno(),
                    HttpStatus.CONFLICT
            );
        }

        TipoEquipo tipo = tipoRepo.findById(dto.getIdTipoEquipo())
                .orElseThrow(() -> new NotFoundException("Tipo de equipo no encontrado"));
        Marca marca = marcaRepo.findById(dto.getIdMarca())
                .orElseThrow(() -> new NotFoundException("Marca no encontrada"));
        Modelo modelo = modeloRepo.findById(dto.getIdModelo())
                .orElseThrow(() -> new NotFoundException("Modelo no encontrado"));

        Equipo entity = EquipoMapers.toEntity(dto);
        entity.setTipoEquipo(tipo);
        entity.setMarca(marca);
        entity.setModelo(modelo);

        // Generar código interno si no viene desde el front
        if (entity.getCodigoInterno() == null || entity.getCodigoInterno().isBlank()) {
            String codigo = generarCodigoInterno(tipo.getPrefijo());
            entity.setCodigoInterno(codigo);
        }

        Equipo guardado = equipoRepository.save(entity);
        return EquipoMapers.toDTO(guardado);
    }

    @Override
    public EquipoDTO update(Long id, EquipoDTO dto) {
        Equipo existing = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con id " + id));

        // Validar número de serie duplicado si cambia
        if (!existing.getNumeroSerie().equals(dto.getNumeroSerie())
                && equipoRepository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new BusinessException(
                    "Ya existe un equipo con número de serie " + dto.getNumeroSerie(),
                    HttpStatus.CONFLICT
            );
        }

        TipoEquipo tipo = tipoRepo.findById(dto.getIdTipoEquipo())
                .orElseThrow(() -> new NotFoundException("Tipo de equipo no encontrado"));
        Marca marca = marcaRepo.findById(dto.getIdMarca())
                .orElseThrow(() -> new NotFoundException("Marca no encontrada"));
        Modelo modelo = modeloRepo.findById(dto.getIdModelo())
                .orElseThrow(() -> new NotFoundException("Modelo no encontrado"));

        // No dejamos cambiar el código interno acá (se mantiene el existente)
        existing.setNombre(dto.getNombre());
        existing.setNumeroSerie(dto.getNumeroSerie());
        existing.setAnioFabricacion(dto.getAnioFabricacion());
        existing.setPotenciaHp(dto.getPotenciaHp());
        existing.setCombustible(dto.getCombustible());
        existing.setEstadoOperativo(dto.getEstadoOperativo());
        existing.setKilometrajeHorasUso(dto.getKilometrajeHorasUso());
        existing.setFechaUltimoMantenimiento(dto.getFechaUltimoMantenimiento());
        existing.setProximoMantenimiento(dto.getProximoMantenimiento());
        existing.setResponsableMantenimiento(dto.getResponsableMantenimiento());
        existing.setNumeroPatente(dto.getNumeroPatente());
        existing.setSeguroVigente(dto.getSeguroVigente());
        existing.setFechaVencimientoSeguro(dto.getFechaVencimientoSeguro());
        existing.setUbicacionActual(dto.getUbicacionActual());
        existing.setActivo(dto.getActivo());
        existing.setLatitud(dto.getLatitud());
        existing.setLongitud(dto.getLongitud());

        existing.setTipoEquipo(tipo);
        existing.setMarca(marca);
        existing.setModelo(modelo);

        Equipo guardado = equipoRepository.save(existing);
        return EquipoMapers.toDTO(guardado);
    }

    @Override
    public void delete(Long id) {
        Equipo existing = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con id " + id));

        // Borrado lógico
        existing.setActivo(false);
        equipoRepository.save(existing);
    }

    // ======================= Helper =======================

    private String generarCodigoInterno(String prefijoTipo) {
        String prefijo = (prefijoTipo == null || prefijoTipo.isBlank())
                ? "EQP"
                : prefijoTipo.toUpperCase();

        return equipoRepository
                .findTopByTipoEquipo_PrefijoOrderByCodigoInternoDesc(prefijo)
                .map(Equipo::getCodigoInterno)
                .map(ultimo -> {
                    String numeroStr = ultimo.substring(prefijo.length());
                    int num;
                    try {
                        num = Integer.parseInt(numeroStr);
                    } catch (NumberFormatException e) {
                        num = 0;
                    }
                    num++;
                    return prefijo + String.format("%04d", num);
                })
                .orElse(prefijo + "0001");
    }

    @Override
    public void actualizarEstado(Long id, String nuevoEstado) {

        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));

        try {
            Estado_Operativo estado = Estado_Operativo.valueOf(nuevoEstado);
            equipo.setEstadoOperativo(estado);
            equipoRepository.save(equipo);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado operativo inválido: " + nuevoEstado);
        }
    }



}
