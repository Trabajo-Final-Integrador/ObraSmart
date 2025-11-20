package obrasmart.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import obrasmart.gestionstock.entity.movimiento.TipoMovimiento;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoStockResponseDto {
    private Long id;
    private Long idRepuesto;
    private String repuestoNombre;
    private String repuestoCodigo;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private String observacion;
    private Instant fecha;
}