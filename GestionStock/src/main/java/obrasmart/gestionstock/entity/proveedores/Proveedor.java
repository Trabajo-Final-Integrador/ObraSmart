package obrasmart.gestionstock.entity.proveedores;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String razonSocial;

    @Column(length = 200)
    private String nombreComercial;

    @Column(nullable = false, unique = true, length = 15)
    private String cuit;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private CondicionIVA condicionIVA;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoProveedor estado;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable=false)
    private String email;

    @Column(length = 100)
    private String personaContacto;

    @Column(length = 100)
    private String horarioAtencion;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String provincia;

    @Column(length = 10)
    private String codigoPostal;

    @Column(length = 100)
    private String especialidad;

    @ElementCollection
    @CollectionTable(name = "proveedor_marcas", joinColumns = @JoinColumn(name = "proveedor_id"))
    @Column(name = "marca")
    @Builder.Default
    private List<String> marcas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private TipoProveedor tipoProveedor;

    private Integer tiempoEntrega; // en días

    @Column(precision = 10, scale = 2)
    private BigDecimal pedidoMinimo;

    @Column(length = 50)
    private String condicionesPago;

    @Column(precision = 5, scale = 2)
    private BigDecimal descuentoVolumen;

    @Builder.Default
    private Boolean tieneStock = false;

    @Builder.Default
    private Boolean haceEnvios = false;

    @Column(length = 200)
    private String zonaCobertura;

    @Builder.Default
    private Boolean aceptaDevoluciones = false;

    // Catálogo y Precios
    @Builder.Default
    private Boolean tieneCatalogo = false;

    @Column(length = 500)
    private String urlCatalogo;

    @Column(length = 50)
    private String codigoCliente;

    private LocalDate fechaUltimaActualizacionPrecios;

    // Datos Bancarios
    @Column(length = 100)
    private String banco;

    @Column(length = 50)
    private String tipoCuenta;

    @Column(length = 22)
    private String cbu;

    // Observaciones
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Métricas
    private LocalDate ultimaCompra;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal totalComprado = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal plazoRespuestaPromedio; // en horas

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaModificacion;



}