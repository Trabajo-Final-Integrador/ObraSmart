package obrasmart.gestionstock.repository;

import obrasmart.gestionstock.entity.repuestos.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {
    boolean existsByCodigo(String codigo);
}