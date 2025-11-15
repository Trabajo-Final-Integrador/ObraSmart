package com.ObraSmart.GestionAsistente.Service.Impl;

import com.ObraSmart.GestionAsistente.Service.IaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IaServiceImpl implements IaService {

    private final RestTemplate restTemplate;

    // =====================
    // IA OLLAMA (Local)
    // =====================
    @Value("${ia.ollama.enabled:true}")
    private boolean ollamaEnabled;

    @Value("${ia.ollama.url:http://localhost:11434/api/generate}")
    private String ollamaUrl;

    @Value("${ia.ollama.model:llama3.2:1b}")
    private String ollamaModel;



    // =====================
    // IA DEEPSEEK (Nube)
    // =====================
    @Value("${ia.deepseek.enabled:false}")
    private boolean deepseekEnabled;

    @Value("${ia.deepseek.url:https://openrouter.ai/api/v1/chat/completions}")
    private String deepseekUrl;

    @Value("${ia.deepseek.model:deepseek-r1:free}")
    private String deepseekModel;

    @Value("${ia.deepseek.apikey:}")
    private String deepseekApiKey;



    public IaServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    @Override
    public String consultarIA(String mensaje) {

        // ============================================
        // PRIORIDAD 1: OLLAMA LOCAL
        // ============================================
        if (ollamaEnabled) {
            try {
                Map<String, Object> request = Map.of(
                        "model", ollamaModel,
                        "prompt", mensaje
                );

                Map response = restTemplate.postForObject(
                        ollamaUrl,
                        request,
                        Map.class
                );

                if (response != null && response.get("response") != null)
                    return response.get("response").toString();

            } catch (Exception ignored) {}
        }



        // ============================================
        // PRIORIDAD 2: DEEPSEEK (OPENROUTER)
        // ============================================
        if (deepseekEnabled && deepseekApiKey != null && !deepseekApiKey.isBlank()) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + deepseekApiKey);

                Map<String, Object> body = Map.of(
                        "model", deepseekModel,
                        "messages", new Object[]{
                                Map.of("role", "user", "content", mensaje)
                        }
                );

                HttpEntity<Object> entity = new HttpEntity<>(body, headers);

                ResponseEntity<Map> response = restTemplate.exchange(
                        deepseekUrl,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

                if (response.getBody() != null) {
                    Map choices = (Map) ((java.util.List) response.getBody().get("choices")).get(0);
                    Map message = (Map) choices.get("message");
                    return message.get("content").toString();
                }

            } catch (Exception ignored) {}
        }



        return null; // la IA falló → vuelve al bot interno
    }

}
