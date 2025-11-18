package obrasmart.gestionstock.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import obrasmart.gestionstock.entity.ordencompra.EstadoOrdenCompra;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraDto {
    private Long id;
    @NotNull private Long idProveedor;
    private EstadoOrdenCompra estado;

    @NotEmpty
    private List<Item> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Item {
        @NotNull private Long idRepuesto;
        @NotNull @Positive private Integer cantidad;
        @NotNull @Positive private Double precioUnitario;
    }
}