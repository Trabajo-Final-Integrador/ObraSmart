package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.AsistenteService;
import com.ObraSmart.GestionAsistente.Service.BotService;
import com.ObraSmart.GestionAsistente.Service.IaService;
import org.springframework.stereotype.Service;

@Service
public class AsistenteServiceImpl implements AsistenteService {

    private final BotService botService;
    private final IaService iaService;

    public AsistenteServiceImpl(BotService botService, IaService iaService) {
        this.botService = botService;
        this.iaService = iaService;
    }

    @Override
    public String procesarMensaje(String mensaje) {

        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "Por favor ingresa un mensaje para que pueda ayudarte.";
        }

        // 1) BOT INTERNO
        String bot = botService.responder(mensaje);
        if (bot != null) return bot;

        // 2) IA (opcional)
        String ia = iaService.consultarIA(mensaje);
        if (ia != null) return ia;

        // 3) Fallback definitivo
        return "No pude interpretar tu consulta. Puedes pedirme: 'crear reparación', 'mapa', 'equipos', 'stock', 'reportes'.";
    }
}
