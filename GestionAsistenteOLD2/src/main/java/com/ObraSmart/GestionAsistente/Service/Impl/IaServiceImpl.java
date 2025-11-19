package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.IaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class IaServiceImpl implements IaService {

    private final RestTemplate rest = new RestTemplate();

    // ====== OLLAMA ======
    @Value("${ia.ollama.enabled:true}")
    private boolean ollamaEnabled;

    @Value("${ia.ollama.url:http://localhost:11434/api/generate}")
    private String ollamaUrl;

    @Value("${ia.ollama.model:llama3.2:1b}")
    private String ollamaModel;


    // ====== DEEPSEEK ======
    @Value("${ia.deepseek.enabled:false}")
    private boolean deepseekEnabled;

    @Value("${ia.deepseek.url:}")
    private String deepseekUrl;

    @Value("${ia.deepseek.model:}")
    private String deepseekModel;

    @Value("${ia.deepseek.apikey:}")
    private String deepseekApiKey;


    // ==========================================================
    //   FLUJO COMPLETO (OLLLAMA -> DEEPSEEK)
    // ==========================================================
    @Override
    public String consultarIA(String mensaje) {

        // 1) IA local
        String local = consultarSoloOllama(mensaje);
        if (local != null) return local;

        // 2) IA avanzada
        String avanzada = consultarSoloDeepseek(mensaje);
        if (avanzada != null) return avanzada;

        return null;
    }


    // ==========================================================
    //   SOLO IA LOCAL
    // ==========================================================
    @Override
    public String consultarSoloOllama(String mensaje) {
        if (!ollamaEnabled) return null;

        try {
            Map<String, Object> body = Map.of(
                    "model", ollamaModel,
                    "prompt", mensaje,
                    "stream", false
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> resp = rest.exchange(
                    ollamaUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (resp.getBody() == null) return null;

            Object raw = resp.getBody().get("response");
            if (raw == null) return null;

            String texto = raw.toString().trim();
            return texto.isBlank() ? null : texto;

        } catch (Exception e) {
            return null;
        }
    }


    // ==========================================================
    //   SOLO IA AVANZADA
    // ==========================================================
    @Override
    public String consultarSoloDeepseek(String mensaje) {
        if (!deepseekEnabled || deepseekApiKey == null || deepseekApiKey.isBlank())
            return null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + deepseekApiKey);

            Map<String, Object> body = Map.of(
                    "model", deepseekModel,
                    "messages", List.of(
                            Map.of("role", "user", "content", mensaje)
                    )
            );

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> resp = rest.exchange(
                    deepseekUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (resp.getBody() == null) return null;

            List<?> choices = (List<?>) resp.getBody().get("choices");
            if (choices == null || choices.isEmpty()) return null;

            Map<?, ?> msg = (Map<?, ?>) ((Map<?, ?>) choices.get(0)).get("message");

            return msg.get("content").toString().trim();

        } catch (Exception e) {
            return null;
        }
    }
}
