package obrasmart.gestionstock.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import obrasmart.gestionstock.entity.proveedores.CondicionIVA;
import obrasmart.gestionstock.entity.proveedores.EstadoProveedor;
import obrasmart.gestionstock.entity.proveedores.TipoProveedor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDto {

    private Long id;

    // Información General
    private String razonSocial;
    private String nombreComercial;
    private String cuit;
    private CondicionIVA condicionIVA;
    private EstadoProveedor estado;
    private Integer rating;

    // Contacto
    private String telefono;
    private String telefonoAlternativo;
    private String email;
    private String personaContacto;
    private String horarioAtencion;

    // Dirección
    private String direccion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;

    // Información Comercial
    private String especialidad;
    private List<String> marcas;
    private TipoProveedor tipoProveedor;
    private Integer tiempoEntrega;
    private BigDecimal pedidoMinimo;
    private String condicionesPago;
    private BigDecimal descuentoVolumen;
    private Boolean tieneStock;
    private Boolean atieneUrgencias;
    private Boolean haceEnvios;
    private String zonaCobertura;
    private Boolean aceptaDevoluciones;

    // Catálogo
    private Boolean tieneCatalogo;
    private String urlCatalogo;
    private String codigoCliente;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaUltimaActualizacionPrecios;

    // Datos Bancarios
    private String banco;
    private String tipoCuenta;
    private String cbu;

    // Observaciones
    private String observaciones;

    // Métricas
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ultimaCompra;
    private BigDecimal totalComprado;
    private BigDecimal plazoRespuestaPromedio;

    // Auditoría
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaAlta;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaModificacion;
}