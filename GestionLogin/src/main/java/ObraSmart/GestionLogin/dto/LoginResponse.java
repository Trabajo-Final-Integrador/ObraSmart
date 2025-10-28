package ObraSmart.GestionLogin.dto;

import ObraSmart.GestionLogin.entity.Role;
import ObraSmart.GestionLogin.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    Long id;
    String username;
    String email;
    Role role;
    Status status;
}
