package obrasmart.gestionstock.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioClient {

    private final RestTemplate restTemplate;

    public UserSession validarSesion(HttpServletRequest request) {
        String cookie = request.getHeader("Cookie");
        if (cookie == null || cookie.isBlank()) {
            log.warn("No se recibio cookie en la request");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.COOKIE, cookie);
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            log.info("Validando sesion en gateway /auth/me con cookie");
            ResponseEntity<java.util.Map> resp =
                    restTemplate.exchange("http://localhost:8085/auth/me",
                            HttpMethod.GET, entity, java.util.Map.class);

            log.info("Respuesta desde auth/me: {} {}", resp.getStatusCode(), resp.getBody());

            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                var body = resp.getBody();
                String username = body.get("userId") != null ? String.valueOf(body.get("userId")) : "user";
                Object rolesObj = body.get("roles");
                String rol = null;
                if (rolesObj instanceof java.util.List<?> list && !list.isEmpty()) {
                    Object first = list.get(0);
                    if (first != null) {
                        rol = first.toString();
                    }
                }
                if (rol != null) {
                    rol = rol.toUpperCase();
                    if (!rol.startsWith("ROLE_")) {
                        rol = "ROLE_" + rol;
                    }
                }
                log.info("Sesion validada via auth/me: usuario={} rol={}", username, rol);
                return new UserSession(username, rol);
            }

            log.warn("auth/me devolvio codigo no exitoso: {}", resp.getStatusCode());
            return null;

        } catch (HttpStatusCodeException e) {
            log.error("Error HTTP al validar sesion: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;

        } catch (Exception e) {
            log.error("Error inesperado al validar sesion", e);
            return null;
        }
    }
}
