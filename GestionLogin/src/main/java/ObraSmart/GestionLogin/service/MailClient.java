package ObraSmart.GestionLogin.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MailClient {
    @Bean
    public RestTemplate restTemplate(){ return new RestTemplate(); }
    public void sendResetEmail(String to, String subject, String body){
// Aquí llamarías a tu microservicio de notificaciones / plataforma de correo.
// Ejemplo simulado (no bloqueante para no fallar si no hay endpoint):
        try { new RestTemplate().postForEntity("https://httpbin.org/post", body, String.class); } catch(Exception ignored){ }
    }
}