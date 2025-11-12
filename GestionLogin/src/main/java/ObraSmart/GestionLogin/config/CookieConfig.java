package ObraSmart.GestionLogin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID"); // Nombre estándar de la cookie de sesión
        serializer.setSameSite("None");         // 🔥 Permite enviar cookie entre localhost:4200, 8081, 8083, etc.
        serializer.setUseSecureCookie(false);   // ⚠️ false porque estás sin HTTPS (localhost)
        serializer.setCookiePath("/");
        return serializer;
    }
}