package obrasmart.gestionstock.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import obrasmart.gestionstock.dto.ProveedorCreateDTO;
import obrasmart.gestionstock.dto.ProveedorDto;
import obrasmart.gestionstock.dto.ProveedorListadoDTO;
import obrasmart.gestionstock.dto.ProveedorUpdateDTO;
import obrasmart.gestionstock.security.UserSession;
import obrasmart.gestionstock.service.ProveedorService;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService service;

    private boolean isAdmin(HttpServletRequest req){
        var us = (UserSession) req.getAttribute("userSession");
        String rol = us != null && us.getRol() != null ? us.getRol().toUpperCase() : "";
        return "ROLE_ADMINISTRACION".equals(rol) || "ADMINISTRACION".equals(rol);
    }

    @GetMapping
    public ResponseEntity<List<ProveedorListadoDTO>> listarLite() {
        return ResponseEntity.ok(service.listarLite());
    }



    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }


    @PostMapping
    public ResponseEntity<ProveedorDto> crear(@Valid @RequestBody ProveedorCreateDTO dto, HttpServletRequest req) {
       // if (!isAdmin(req)) return ResponseEntity.status(403).build();
        log.info("📥 DTO recibido en controller: {}", dto);
        ProveedorDto result = service.crear(dto);
        log.info("✅ Respuesta enviada al frontend: {}", result);
        return ResponseEntity.ok(result);

        //return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDto> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorUpdateDTO dto, HttpServletRequest req) {
        if (!isAdmin(req)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, HttpServletRequest req) {
        if (!isAdmin(req)) return ResponseEntity.status(403).build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, String>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            HttpServletRequest req) {

        service.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(Map.of("message", "Estado actualizado exitosamente"));
    }





    @GetMapping("/exists/cuit/{cuit}")
    public ResponseEntity<Map<String, Boolean>> existsByCuit(@PathVariable String cuit) {

        boolean exists = service.existsByCuit(cuit);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
