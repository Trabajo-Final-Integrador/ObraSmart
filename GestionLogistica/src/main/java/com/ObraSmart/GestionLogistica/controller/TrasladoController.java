package com.ObraSmart.GestionLogistica.controller;

import com.ObraSmart.GestionLogistica.dto.AuthIdentityDto;
import com.ObraSmart.GestionLogistica.dto.TrasladoRequestDto;
import com.ObraSmart.GestionLogistica.dto.TrasladoResponseDto;
import com.ObraSmart.GestionLogistica.service.AuthClient;
import com.ObraSmart.GestionLogistica.service.TrasladoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/traslados")
@RequiredArgsConstructor
public class TrasladoController {

    private static final String ROLE_SUPERVISOR = "SUPERVISOR";

    private final TrasladoService trasladoService;
    private final AuthClient authClient;

    @GetMapping
    public List<TrasladoResponseDto> listar() {
        return trasladoService.listar();
    }

    @GetMapping("/{id}")
    public TrasladoResponseDto obtener(@PathVariable Long id) {
        return trasladoService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrasladoResponseDto crear(@Valid @RequestBody TrasladoRequestDto dto, HttpServletRequest request) {
        verificarSupervisor(request);
        return trasladoService.crear(dto);
    }

    @PatchMapping("/{id}/estado")
    public TrasladoResponseDto cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            HttpServletRequest request
    ) {
        verificarSupervisor(request);
        return trasladoService.cambiarEstado(id, estado);
    }

    private void verificarSupervisor(HttpServletRequest request) {
        AuthIdentityDto identity = authClient.fetchIdentity(request);
        if (identity == null || identity.roles() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        boolean supervisor = identity.roles().stream()
                .filter(r -> r != null)
                .anyMatch(r -> r.equalsIgnoreCase(ROLE_SUPERVISOR));

        if (!supervisor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo rol SUPERVISOR puede gestionar traslados");
        }
    }
}
