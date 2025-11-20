package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.AsistenteService;
import com.ObraSmart.GestionAsistente.Service.BotService;
import com.ObraSmart.GestionAsistente.Service.IA.IaCloudService;
import com.ObraSmart.GestionAsistente.Service.IA.IaLocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsistenteServiceImpl implements AsistenteService {

    private final BotService bot;
    private final IaLocalService iaLocal;
    private final IaCloudService iaCloud;

    @Override
    public String procesarMensaje(String mensaje) {

        if (mensaje == null || mensaje.isBlank()) {
            return formatear("⚠️ Error", "No recibí ningún mensaje. ¿Podés repetirlo?");
        }

        // ============================
        // 1️⃣ BOT
        // ============================
        String rBot = bot.responder(mensaje);

        if ("__FORZAR_OLLAMA__".equals(rBot)) {
            return formatear("🧠 IA Local (forzado)", iaLocal.responder(mensaje));
        }

        if ("__FORZAR_CLOUD__".equals(rBot)) {
            return formatear("🌐 IA Cloud (forzado)", iaCloud.responder(mensaje));
        }

        if (rBot != null) {
            return formatear("🤖 Respuesta rápida", rBot);
        }

        // ============================
        // 2️⃣ PREGUNTA CORTA → LOCAL
        // ============================
        boolean esPreguntaSimple = mensaje.length() <= 40;

        if (esPreguntaSimple) {
            String local = iaLocal.responder(mensaje);

            if (local != null && !local.isBlank()) {
                return formatear("🧠 IA Local", local);
            }
        }

        // ============================
        // 3️⃣ CLOUD → si respuesta compleja
        // ============================
        String cloud = iaCloud.responder(mensaje);
        if (cloud != null && !cloud.isBlank()) {
            return formatear("🌐 IA Cloud", cloud);
        }

        // ============================
        // 4️⃣ ULTIMO RECURSO → LOCAL
        // ============================
        String localFallback = iaLocal.responder(mensaje);
        if (localFallback != null && !localFallback.isBlank()) {
            return formatear("🧠 IA Local (offline)", localFallback);
        }

        return formatear("❓ No entendí",
                "No pude procesar eso. Podés intentar reformular la pregunta.");
    }

    private String formatear(String origen, String cuerpo) {
        return ("%s\n%s").formatted(origen, cuerpo.trim());
    }
}
