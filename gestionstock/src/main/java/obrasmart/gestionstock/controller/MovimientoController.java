package obrasmart.gestionstock.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.MovimientoStockDto;
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
    public ResponseEntity<MovimientoStockDto> registrar(@Valid @RequestBody MovimientoStockDto dto, HttpServletRequest req){
        if (!isAdminOrTecnico(req)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoStockDto>> listar(HttpServletRequest req){
        if (!isAdminOrTecnico(req)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.listar());
    }
}