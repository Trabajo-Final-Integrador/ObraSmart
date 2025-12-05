package obrasmart.gestionstock.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import obrasmart.gestionstock.entity.proveedores.CondicionIVA;
import obrasmart.gestionstock.entity.proveedores.TipoProveedor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorUpdateDTO {

    @Size(max = 200)
    private String razonSocial;

    @Size(max = 200)
    private String nombreComercial;

    @Pattern(regexp = "^\\d{2}-\\d{8}-\\d{1}$", message = "El CUIT debe tener el formato XX-XXXXXXXX-X")
    private String cuit;

    private CondicionIVA condicionIVA;

    @Size(max = 20)
    private String telefono;

    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String personaContacto;

    @Size(max = 100)
    private String horarioAtencion;

    @Size(max = 200)
    private String direccion;

    @Size(max = 100)
    private String ciudad;

    @Size(max = 100)
    private String provincia;

    @Size(max = 10)
    private String codigoPostal;

    @Size(max = 100)
    private String especialidad;

    private List<String> marcas;

    @Size(max = 100)
    private TipoProveedor tipoProveedor;

    @Min(0)
    private Integer tiempoEntrega;

    @DecimalMin("0.0")
    private BigDecimal pedidoMinimo;

    @Size(max = 50)
    private String condicionesPago;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal descuentoVolumen;

    private Boolean tieneStock;
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

    @Min(0)
    @Max(5)
    private Integer rating;

    private String estado; // ACTIVO, INACTIVO, BLOQUEADO
}