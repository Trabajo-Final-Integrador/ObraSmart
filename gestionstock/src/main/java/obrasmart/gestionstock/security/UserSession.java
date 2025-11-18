package obrasmart.gestionstock.security;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSession {
    private String username;
    private String rol; // "ROLE_ADMINISTRADOR" o "ROLE_TECNICO" (según tu login)
}