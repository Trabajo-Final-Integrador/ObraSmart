package com.ObraSmart.GestionObrador.controller;

import com.ObraSmart.GestionObrador.dto.AsignarEquipoDto;
import com.ObraSmart.GestionObrador.dto.ObradorRequestDto;
import com.ObraSmart.GestionObrador.dto.ObradorResponseDto;
import com.ObraSmart.GestionObrador.service.ObradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/obradores")
@RequiredArgsConstructor
public class ObradorController {

    private final ObradorService obradorService;

    @GetMapping
    public List<ObradorResponseDto> listar() {
        return obradorService.listar();
    }

    @GetMapping("/{id}")
    public ObradorResponseDto obtenerPorId(@PathVariable Long id) {
        return obradorService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ObradorResponseDto crear(@Valid @RequestBody ObradorRequestDto dto) {
        return obradorService.crear(dto);
    }

    @PutMapping("/{id}")
    public ObradorResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody ObradorRequestDto dto) {
        return obradorService.actualizar(id, dto);
    }

    @PostMapping("/{id}/equipos")
    public ObradorResponseDto asignarEquipo(@PathVariable Long id, @Valid @RequestBody AsignarEquipoDto dto) {
        return obradorService.asignarEquipo(id, dto);
    }
}
