package obrasmart.gestionstock.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRepuestoDto {
    private Long id;
    @NotBlank private String nombre;
}