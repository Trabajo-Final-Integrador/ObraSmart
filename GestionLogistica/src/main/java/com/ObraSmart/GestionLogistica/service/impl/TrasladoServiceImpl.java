package com.ObraSmart.GestionLogistica.service.impl;

import com.ObraSmart.GestionLogistica.dto.TrasladoRequestDto;
import com.ObraSmart.GestionLogistica.dto.TrasladoResponseDto;
import com.ObraSmart.GestionLogistica.entity.TrasladoEquipo;
import com.ObraSmart.GestionLogistica.entity.TrasladoEquipo.EstadoTraslado;
import com.ObraSmart.GestionLogistica.repository.TrasladoRepository;
import com.ObraSmart.GestionLogistica.service.TrasladoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TrasladoServiceImpl implements TrasladoService {

    private final TrasladoRepository trasladoRepository;

    @Override
    public List<TrasladoResponseDto> listar() {
        return trasladoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TrasladoResponseDto obtenerPorId(Long id) {
        return toResponse(buscar(id));
    }

    @Override
    public TrasladoResponseDto crear(TrasladoRequestDto dto) {
        TrasladoEquipo entity = TrasladoEquipo.builder()
                .equipoId(dto.equipoId())
                .origenObradorId(dto.origenObradorId())
                .destinoObradorId(dto.destinoObradorId())
                .programadoPara(dto.programadoPara())
                .notas(dto.notas())
                .estado(EstadoTraslado.PENDIENTE)
                .build();
        return toResponse(trasladoRepository.save(entity));
    }

    @Override
    public TrasladoResponseDto cambiarEstado(Long id, String nuevoEstado) {
        TrasladoEquipo entity = buscar(id);
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado requerido");
        }

        EstadoTraslado estado = parseEstado(nuevoEstado);
        entity.setEstado(estado);

        return toResponse(trasladoRepository.save(entity));
    }

    private TrasladoEquipo buscar(Long id) {
        return trasladoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Traslado no encontrado"));
    }

    private TrasladoResponseDto toResponse(TrasladoEquipo entity) {
        return new TrasladoResponseDto(
                entity.getId(),
                entity.getEquipoId(),
                entity.getOrigenObradorId(),
                entity.getDestinoObradorId(),
                entity.getProgramadoPara(),
                entity.getFechaCreacion(),
                entity.getEstado(),
                entity.getNotas()
        );
    }

    private EstadoTraslado parseEstado(String valor) {
        try {
            return EstadoTraslado.valueOf(valor.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de traslado invalido");
        }
    }
}
