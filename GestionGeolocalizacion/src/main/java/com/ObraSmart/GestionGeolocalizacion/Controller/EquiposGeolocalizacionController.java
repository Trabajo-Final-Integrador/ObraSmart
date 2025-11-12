package com.ObraSmart.GestionGeolocalizacion.Controller;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionResponse;
import com.ObraSmart.GestionGeolocalizacion.Service.IUbicacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geolocalizacion")
public class EquiposGeolocalizacionController {

    private final IUbicacionesService ubicaciones;

    public EquiposGeolocalizacionController(IUbicacionesService ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    @GetMapping("/locations")
    public ResponseEntity<List<EquipoUbicacionResponse>> listar() {
        return ResponseEntity.ok(ubicaciones.listarParaMapa());
    }

    @GetMapping("/ubicaciones")
    public ResponseEntity<List<EquipoUbicacionResponse>> alias() {
        return ResponseEntity.ok(ubicaciones.listarParaMapa());
    }

    //para probar en mapa
    @GetMapping("/test")
    public ResponseEntity<List<EquipoUbicacionResponse>> testData() {
        List<EquipoUbicacionResponse> lista = List.of(
                new EquipoUbicacionResponse(1L, "Retroexcavadora CAT 320", -34.6037, -58.3816, "EN_REPARACION"),
                new EquipoUbicacionResponse(2L, "Grúa Grove 4100", -34.6200, -58.4400, "OPERATIVO"),
                new EquipoUbicacionResponse(3L, "Camión Mixer 12T", -34.5800, -58.4200, "DESCONOCIDO"),
                new EquipoUbicacionResponse(4L, "Excavadora ZX200", -34.6170, -58.3810, "EN_REPARACION"),
                new EquipoUbicacionResponse(5L, "Pala Cargadora Komatsu WA200", -34.6105, -58.3850, "OPERATIVO"),
                new EquipoUbicacionResponse(6L, "Hormigonera Liebherr", -34.5990, -58.3730, "DESCONOCIDO")
        );

        return ResponseEntity.ok(lista);
    }

}
