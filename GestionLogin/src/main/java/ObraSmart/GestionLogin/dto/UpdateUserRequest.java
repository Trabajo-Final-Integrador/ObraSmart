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
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @Email
    @NotBlank String email;
    @NotNull Role role;
    @NotNull Status status;
}
