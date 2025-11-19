package com.ObraSmart.GestionAsistente.Controller;

import com.ObraSmart.GestionAsistente.Dto.RequestDto;
import com.ObraSmart.GestionAsistente.Dto.ResponseDto;
import com.ObraSmart.GestionAsistente.Service.AsistenteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asistente")
public class AsistenteController {

    private final AsistenteService asistenteService;

    public AsistenteController(AsistenteService asistenteService) {
        this.asistenteService = asistenteService;
    }

    @PostMapping("/chat")
    public ResponseDto chat(@RequestBody RequestDto dto) {
        String respuesta = asistenteService.procesarMensaje(dto.mensaje());
        return new ResponseDto(respuesta);
    }
}
