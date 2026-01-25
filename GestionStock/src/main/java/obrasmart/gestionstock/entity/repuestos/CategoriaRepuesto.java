package obrasmart.gestionstock.entity.repuestos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="categorias_repuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRepuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String nombre;
}