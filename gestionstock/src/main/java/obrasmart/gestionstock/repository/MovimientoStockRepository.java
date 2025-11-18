package obrasmart.gestionstock.repository;

import obrasmart.gestionstock.entity.movimiento.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {}