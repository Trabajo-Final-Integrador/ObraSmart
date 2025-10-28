package ObraSmart.GestionLogin.dto;

import ObraSmart.GestionLogin.entity.Role;
import ObraSmart.GestionLogin.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank String username;
    @Email
    @NotBlank String email;
    @NotBlank String password;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotNull Role role;
    @NotNull Status status;
}
