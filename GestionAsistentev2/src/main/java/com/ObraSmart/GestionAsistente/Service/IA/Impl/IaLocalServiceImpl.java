package com.ObraSmart.GestionAsistente.Service.IA.Impl;

import com.ObraSmart.GestionAsistente.Service.IA.IaLocalService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class IaLocalServiceImpl implements IaLocalService {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${ia.ollama.enabled:true}")
    private boolean enabled;

    @Value("${ia.ollama.url}")
    private String url;

    @Value("${ia.ollama.model}")
    private String model;

    @Override
    public String responder(String mensaje) {
        if (!enabled) return null;

        try {
            String json = """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": false
                }
                """.formatted(model, mensaje.replace("\"", "'"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<String> resp = rest.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(json, headers),
                    String.class
            );

            if (!resp.getStatusCode().is2xxSuccessful()) {
                log.error("❌ Ollama devolvió HTTP {}", resp.getStatusCode());
                return null;
            }

            JsonNode root = mapper.readTree(resp.getBody());
            JsonNode r = root.path("response");

            if (r.isMissingNode() || r.asText().isBlank()) {
                log.warn("⚠️ Ollama devolvió respuesta vacía");
                return null;
            }

            return r.asText();

        } catch (Exception e) {
            log.error("❌ Error al llamar a Ollama", e);
            return null;
        }
    }
}
