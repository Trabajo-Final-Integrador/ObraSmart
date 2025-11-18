package obrasmart.gestionstock.repository;

import obrasmart.gestionstock.entity.ordencompra.OrdenCompraItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenCompraItemRepository extends JpaRepository<OrdenCompraItem, Long> {}