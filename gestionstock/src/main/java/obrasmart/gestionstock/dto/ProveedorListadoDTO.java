package obrasmart.gestionstock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import obrasmart.gestionstock.entity.proveedores.EstadoProveedor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorListadoDTO {

    private Long id;
    private String razonSocial;
    private String especialidad;
    private String contacto;
    private EstadoProveedor estado;
}