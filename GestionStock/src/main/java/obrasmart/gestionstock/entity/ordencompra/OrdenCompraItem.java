package obrasmart.gestionstock.entity.ordencompra;


import jakarta.persistence.*;
import lombok.*;
import obrasmart.gestionstock.entity.ordencompra.OrdenCompra;
import obrasmart.gestionstock.entity.repuestos.Repuesto;

@Entity
@Table(name="ordenes_compra_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private OrdenCompra orden;

    @ManyToOne(optional=false)
    private Repuesto repuesto;

    @Column(nullable=false)
    private Integer cantidad;

    @Column(nullable=false)
    private Double precioUnitario;
}