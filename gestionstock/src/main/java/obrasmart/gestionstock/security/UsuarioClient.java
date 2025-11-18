package obrasmart.gestionstock.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
            log.warn("❌ No se recibió cookie en la request");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.COOKIE, cookie);
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            log.info("🔎 Validando sesión en login-service con cookie: {}", cookie);
            ResponseEntity<java.util.Map> resp =
                    restTemplate.exchange("http://localhost:8081/users/auth/id",
                            HttpMethod.GET, entity, java.util.Map.class);

            log.info("📡 Respuesta desde login-service: {} {}", resp.getStatusCode(), resp.getBody());

            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                var body = resp.getBody();
                String username = (String) body.get("username");
                String rol = (String) body.get("rol");
                if ("ADMINISTRACION".equalsIgnoreCase(rol)) {
                    rol = "ADMINISTRADOR";
                }
                if (rol != null && !rol.startsWith("ROLE_")) {
                    rol = "ROLE_" + rol;
                }
                log.info("✅ Sesión validada: usuario={} rol={}", username, rol);
                return new UserSession(username, rol);
            }

            log.warn("⚠️ Login-service devolvió código no exitoso: {}", resp.getStatusCode());
            return null;

        } catch (HttpStatusCodeException e) {
            log.error("❌ Error HTTP al validar sesión: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;

        } catch (Exception e) {
            log.error("💥 Error inesperado al validar sesión", e);
            return null;
        }
    }
}