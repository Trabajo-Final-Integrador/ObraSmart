package obrasmart.gestionstock.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import obrasmart.gestionstock.entity.ordencompra.EstadoOrdenCompra;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraDto {
    private Long id;
    @NotNull private Long idProveedor;
    private String proveedorNombre;

    private EstadoOrdenCompra estado;
    private Instant fecha;
    private Integer totalItems;
    private Double total;

    @NotEmpty
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Item {
        @NotNull private Long idRepuesto;
        private String repuestoNombre;
        @NotNull @Positive private Integer cantidad;
        @NotNull @Positive private Double precioUnitario;
    }
}