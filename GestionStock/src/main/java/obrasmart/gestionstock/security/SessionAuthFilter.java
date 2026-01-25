package obrasmart.gestionstock.security;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionAuthFilter implements Filter {

    /*private final UsuarioClient usuarioClient;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) req;
        HttpServletResponse resp = (HttpServletResponse) res;

        String path = http.getRequestURI();

// 💥 Excluir proveedores del filtro
        if (path.startsWith("/proveedores")) {
            chain.doFilter(req, res);
            return;
        }

        // 🧩 LOG PARA DEPURAR COOKIES
        log.info("🧩 Path solicitado: {}", path);
        log.info("🧩 Cookies recibidas: {}", http.getHeader("Cookie"));

        // Endpoints públicos en este micro:
        boolean publico = path.startsWith("/error") || path.startsWith("/actuator");
        if (publico) {
            chain.doFilter(req, res);
            return;
        }

        // Sólo protegemos /api/**
        if (!path.startsWith("/api/")) {
            chain.doFilter(req, res);
            return;
        }

        UserSession us = usuarioClient.validarSesion(http);
        if (us == null) {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        // Guardamos el user/rol para controles de rol en controllers
        http.setAttribute("userSession", us);
        log.info("✅ Sesión validada correctamente, continuando hacia controller...");
        chain.doFilter(req, res);
    }*/
    private final UsuarioClient usuarioClient;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) req;
        HttpServletResponse resp = (HttpServletResponse) res;

        String path = http.getRequestURI();

        log.info("🧩 Path solicitado: {}", path);

        // ===========================================================
        // 1) ENDPOINTS PÚBLICOS
        // ===========================================================
        if (path.startsWith("/error") || path.startsWith("/actuator")) {
            chain.doFilter(req, res);
            return;
        }

        // ===========================================================
        // 2) VALIDAR SESIÓN PARA TODO
        // ===========================================================
        UserSession us = usuarioClient.validarSesion(http);

        if (us == null) {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        http.setAttribute("userSession", us);

        // ===========================================================
        // 3) CONTROL DE ROL (ADMIN) PARA PROVEEDORES
        // ===========================================================
        String rol = us.getRol() != null ? us.getRol().toUpperCase() : "";
        if (path.startsWith("/proveedores") && !"ROLE_ADMINISTRACION".equals(rol)) {
            resp.setStatus(HttpStatus.FORBIDDEN.value());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Forbidden: solo administradores\"}");
            return;
        }

        // Continua al controller
        chain.doFilter(req, res);
    }
}
