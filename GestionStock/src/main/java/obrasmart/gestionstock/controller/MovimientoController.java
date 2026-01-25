package obrasmart.gestionstock.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.MovimientoStockDto;
import obrasmart.gestionstock.dto.MovimientoStockResponseDto;
import obrasmart.gestionstock.security.UserSession;
import obrasmart.gestionstock.service.MovimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/movimientos") @RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService service;

    private boolean isAdminOrTecnico(HttpServletRequest req){
        var us = (UserSession) req.getAttribute("userSession");
        if (us == null) return false;
        return "ROLE_ADMINISTRADOR".equals(us.getRol()) || "ROLE_TECNICO".equals(us.getRol());
    }

    @PostMapping
    public ResponseEntity<MovimientoStockResponseDto> registrar(@Valid @RequestBody MovimientoStockDto dto) {
        return ResponseEntity.status(201).body(service.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoStockResponseDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}