package obrasmart.gestionstock.entity.ordencompra;


import jakarta.persistence.*;
import lombok.*;
import obrasmart.gestionstock.entity.proveedores.Proveedor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ordenes_compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_orden", unique = true, length = 12, nullable = false)
    private String codigoOrden;


    @ManyToOne(optional=false) private Proveedor proveedor;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private EstadoOrdenCompra estado = EstadoOrdenCompra.PENDIENTE;

    @Column(nullable=false) private Instant fecha = Instant.now();

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenCompraItem> items = new ArrayList<>();
}