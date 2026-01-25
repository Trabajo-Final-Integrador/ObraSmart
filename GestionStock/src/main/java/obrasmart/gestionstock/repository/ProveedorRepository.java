package obrasmart.gestionstock.repository;


import obrasmart.gestionstock.entity.proveedores.EstadoProveedor;
import obrasmart.gestionstock.entity.proveedores.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {



    Optional<Proveedor> findByCuit(String cuit);


    boolean existsByCuit(String cuit);

    List<Proveedor> findByEstado(EstadoProveedor estado);

    @Query("SELECT p FROM Proveedor p WHERE " +
            "LOWER(p.nombreComercial) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.cuit) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.especialidad) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Proveedor> searchProveedores(@Param("search") String search);

    @Query("SELECT p FROM Proveedor p WHERE p.estado = :estado AND " +
            "(LOWER(p.nombreComercial) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Proveedor> searchByEstadoAndText(
            @Param("estado") EstadoProveedor estado,
            @Param("search") String search
    );
}