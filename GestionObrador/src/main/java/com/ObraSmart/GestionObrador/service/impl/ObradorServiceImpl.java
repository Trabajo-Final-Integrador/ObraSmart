package com.ObraSmart.GestionObrador.service.impl;

import com.ObraSmart.GestionObrador.dto.AsignarEquipoDto;
import com.ObraSmart.GestionObrador.dto.ObradorRequestDto;
import com.ObraSmart.GestionObrador.dto.ObradorResponseDto;
import com.ObraSmart.GestionObrador.entity.Obrador;
import com.ObraSmart.GestionObrador.repository.ObradorRepository;
import com.ObraSmart.GestionObrador.service.ObradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObradorServiceImpl implements ObradorService {

    private final ObradorRepository obradorRepository;

    @Override
    public List<ObradorResponseDto> listar() {
        return obradorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ObradorResponseDto obtenerPorId(Long id) {
        return toResponse(buscarObrador(id));
    }

    @Override
    public ObradorResponseDto crear(ObradorRequestDto dto) {
        Obrador obrador = Obrador.builder()
                .nombre(dto.nombre())
                .ubicacion(dto.ubicacion())
                .latitud(dto.latitud())
                .longitud(dto.longitud())
                .supervisorUserId(dto.supervisorUserId())
                .equipoIds(dto.equipoIds() != null ? new HashSet<>(dto.equipoIds()) : new HashSet<>())
                .build();
        return toResponse(obradorRepository.save(obrador));
    }

    @Override
    public ObradorResponseDto actualizar(Long id, ObradorRequestDto dto) {
        Obrador obrador = buscarObrador(id);
        obrador.setNombre(dto.nombre());
        obrador.setUbicacion(dto.ubicacion());
        obrador.setLatitud(dto.latitud());
        obrador.setLongitud(dto.longitud());
        obrador.setSupervisorUserId(dto.supervisorUserId());

        if (dto.equipoIds() != null) {
            obrador.setEquipoIds(new HashSet<>(dto.equipoIds()));
        }

        return toResponse(obradorRepository.save(obrador));
    }

    @Override
    public ObradorResponseDto asignarEquipo(Long id, AsignarEquipoDto dto) {
        Obrador obrador = buscarObrador(id);
        if (dto.equipoId() != null) {
            obrador.getEquipoIds().add(dto.equipoId());
        }
        return toResponse(obradorRepository.save(obrador));
    }

    private Obrador buscarObrador(Long id) {
        return obradorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obrador no encontrado"));
    }

    private ObradorResponseDto toResponse(Obrador obrador) {
        return new ObradorResponseDto(
                obrador.getId(),
                obrador.getNombre(),
                obrador.getUbicacion(),
                obrador.getLatitud(),
                obrador.getLongitud(),
                obrador.getSupervisorUserId(),
                new HashSet<>(obrador.getEquipoIds())
        );
    }
}
