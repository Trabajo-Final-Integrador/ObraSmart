package obrasmart.gestionstock.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoDto {
    private Long id;

    @NotBlank private String codigo;
    @NotBlank private String nombre;

    @NotNull private Long idCategoria;

    @NotNull private Integer stock;
    @NotNull private Integer stockMinimo;

    @NotBlank private String unidadMedida;
}