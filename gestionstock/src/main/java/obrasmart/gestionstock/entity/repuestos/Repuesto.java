package obrasmart.gestionstock.entity.repuestos;


import jakarta.persistence.*;
import lombok.*;
import obrasmart.gestionstock.entity.repuestos.CategoriaRepuesto;

@Entity
@Table(name="repuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repuesto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String codigo;

    @Column(nullable=false)
    private String nombre;

    @ManyToOne(optional = false)
    private CategoriaRepuesto categoria;

    @Column(nullable=false)
    private Integer stock = 0;

    @Column(nullable=false)
    private Integer stockMinimo = 0;
}
