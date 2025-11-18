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
            return mensajeBienvenida();
        }

        mensaje = mensaje.trim().toLowerCase();

        // --- INFO DEL ASISTENTE ---
        if (mensaje.equals("info")) {
            return infoAsistente();
        }

        // --- 1️⃣ BOT INTERNO ---
        String bot = botService.responder(mensaje);
        if (bot != null) return bot + firma();

        // --- 2️⃣ IA LOCAL (Ollama) ---
        String local = iaService.consultarSoloOllama(mensaje);
        if (local != null) {
            return "🧠 IA local respondiendo:\n" + local + firma();
        }

        // --- 3️⃣ IA AVANZADA (solo si está habilitada) ---
        String avanzada = iaService.consultarSoloDeepseek(mensaje);
        if (avanzada != null) {
            return "🌐 IA avanzada respondiendo:\n" + avanzada + firma();
        }

        // --- 4️⃣ FALLBACK ---
        return """
                ⚠ No pude interpretar tu consulta.

                Prueba con:
                • crear reparación
                • mapa
                • equipos
                • stock
                • reportes
                """ + firma();
    }



    // ======================================================
    // TEXTOS
    // ======================================================
    private String mensajeBienvenida() {
        return """
                🤖 Bienvenido al Asistente de ObraSmart

                Escribe *info* para ver la guía del asistente.
                Preguntá lo que necesites.

                — ObraSmart —
                """ + firma();
    }

    private String infoAsistente() {
        return """
                📘 **Guía del Asistente de ObraSmart**

                El asistente funciona usando:

                1️⃣ Bot interno  
                Responde tareas de ObraSmart:
                • crear reparación
                • equipos / estados
                • mapa
                • mantenimiento
                • stock
                • reportes
                • tutorial / manual

                2️⃣ IA Local (Ollama)  
                • Explicaciones simples  
                • Reformulaciones  
                • Conceptos básicos  

                3️⃣ IA Avanzada (DeepSeek)*  
                *Solo si estuviera habilitada

                El asistente elige automáticamente la mejor opción.
                """ + firma();
    }

    private String firma() {
        return """

                ———————————————
                Asistente Desarrollado por:
                • FGF&LSCA   
                """;
    }
}
