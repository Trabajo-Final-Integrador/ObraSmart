package ObraSmart.GestionLogin.dto;

import ObraSmart.GestionLogin.entity.Role;
import ObraSmart.GestionLogin.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    Long id;
    String username;
    String email;
    Role role;
    Status status;
}
