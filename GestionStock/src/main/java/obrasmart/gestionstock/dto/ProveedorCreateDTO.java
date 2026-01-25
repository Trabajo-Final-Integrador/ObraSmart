package obrasmart.gestionstock.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import obrasmart.gestionstock.entity.proveedores.CondicionIVA;
import obrasmart.gestionstock.entity.proveedores.EstadoProveedor;
import obrasmart.gestionstock.entity.proveedores.Proveedor;
import obrasmart.gestionstock.entity.proveedores.TipoProveedor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorCreateDTO {



    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "La razón social no puede tener más de 200 caracteres")
    private String razonSocial;

    @Size(max = 200, message = "El nombre comercial no puede tener más de 200 caracteres")
    private String nombreComercial;

    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(regexp = "^\\d{2}-\\d{8}-\\d{1}$", message = "El CUIT debe tener el formato XX-XXXXXXXX-X")
    private String cuit;

    @NotNull(message = "La condición de IVA es obligatoria")
    private CondicionIVA condicionIVA;

    @NotNull(message = "El estado del proveedor es obligatorio")
    private EstadoProveedor estado;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20)
    private String telefono;



    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String personaContacto;

    @Size(max = 100)
    private String horarioAtencion;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100)
    private String ciudad;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 100)
    private String provincia;

    @Size(max = 10)
    private String codigoPostal;

    @Size(max = 100)
    private String especialidad;

    private List<String> marcas;


    private TipoProveedor tipoProveedor;

    @Min(value = 0, message = "El tiempo de entrega no puede ser negativo")
    private Integer tiempoEntrega;

    @DecimalMin(value = "0.0", message = "El pedido mínimo no puede ser negativo")
    private BigDecimal pedidoMinimo;

    @Size(max = 50)
    private String condicionesPago;

    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El descuento no puede ser mayor a 100")
    private BigDecimal descuentoVolumen;

    private Boolean tieneStock;
    private Boolean atieneUrgencias;
    private Boolean haceEnvios;

    @Size(max = 200)
    private String zonaCobertura;

    private Boolean aceptaDevoluciones;
    private Boolean tieneCatalogo;

    @Size(max = 500)
    private String urlCatalogo;

    @Size(max = 50)
    private String codigoCliente;

    @Size(max = 100)
    private String banco;

    @Size(max = 50)
    private String tipoCuenta;

    @Pattern(regexp = "^[0-9]{22}$", message = "El CBU debe tener 22 dígitos")
    private String cbu;

    private String observaciones;

}