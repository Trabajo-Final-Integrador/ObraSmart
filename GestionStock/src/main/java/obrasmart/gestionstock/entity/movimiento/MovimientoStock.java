package obrasmart.gestionstock.entity.movimiento;



import jakarta.persistence.*;
import lombok.*;
import obrasmart.gestionstock.entity.repuestos.Repuesto;

import java.time.Instant;

@Entity
@Table(name="movimientos_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) private Repuesto repuesto;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private TipoMovimiento tipo;

    @Column(nullable=false)
    private Integer cantidad;

    @Column(nullable=false)
    private Instant fecha = Instant.now();

    private String observacion;
}