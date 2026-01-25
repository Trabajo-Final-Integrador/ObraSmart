package obrasmart.gestionstock.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import obrasmart.gestionstock.entity.movimiento.TipoMovimiento;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoStockDto {
    private Long id;
    @NotNull private Long idRepuesto;
    @NotNull private TipoMovimiento tipo;
    @NotNull @Positive private Integer cantidad;
    private String observacion;
}