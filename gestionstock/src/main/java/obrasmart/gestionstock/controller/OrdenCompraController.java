package obrasmart.gestionstock.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.OrdenCompraDto;
import obrasmart.gestionstock.dto.OrdenCompraListadoDTO;
import obrasmart.gestionstock.security.UserSession;
import obrasmart.gestionstock.service.OrdenCompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/ordenes") @RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService service;

    private boolean isAdmin(HttpServletRequest req){
        var us = (UserSession) req.getAttribute("userSession");
        return us != null && "ROLE_ADMINISTRADOR".equals(us.getRol());
    }

    @PostMapping
    public ResponseEntity<OrdenCompraDto> crear(@Valid @RequestBody OrdenCompraDto dto, HttpServletRequest req){
        if (!isAdmin(req)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrdenCompraListadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<OrdenCompraDto> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprobar(id));
    }

    @PostMapping("/{id}/recibir")
    public ResponseEntity<OrdenCompraDto> recibir(@PathVariable Long id) {
        return ResponseEntity.ok(service.recibir(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<OrdenCompraDto> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }


}