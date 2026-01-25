package obrasmart.gestionstock.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenCompraListadoDTO {
    private Long id;

    private Long idProveedor;
    private String proveedorNombre;

    private String estado;

    private Integer totalItems;
    private Double total;

    private Instant fecha;
}