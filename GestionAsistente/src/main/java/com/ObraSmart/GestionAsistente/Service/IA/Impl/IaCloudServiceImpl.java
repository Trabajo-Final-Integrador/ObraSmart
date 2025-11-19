package com.ObraSmart.GestionAsistente.Service.IA.Impl;

import com.ObraSmart.GestionAsistente.Service.IA.IaCloudService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class IaCloudServiceImpl implements IaCloudService {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${ia.cloud.enabled:false}")
    private boolean enabled;

    @Value("${ia.cloud.url}")
    private String url;

    @Value("${ia.cloud.apikey:}")
    private String apiKey;

    @Value("${ia.cloud.model}")
    private String model;

    @Override
    public String responder(String mensaje) {

        if (!enabled || apiKey == null || apiKey.isBlank())
            return null;

        try {
            String json = """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "system",
                      "content": "Sos el asistente técnico oficial de ObraSmart. Respondé SIEMPRE en español claro y profesional."
                    },
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(model, mensaje.replace("\"", "'"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            ResponseEntity<String> resp = rest.postForEntity(url, entity, String.class);

            if (resp.getBody() == null) return null;

            JsonNode jsonNode = mapper.readTree(resp.getBody());
            return jsonNode.path("choices").path(0).path("message").path("content").asText();

        } catch (Exception e) {
            log.error("❌ Error en IA Cloud", e);
            return null;
        }
    }
}
