package obrasmart.gestionstock.repository;

import obrasmart.gestionstock.entity.ordencompra.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {}