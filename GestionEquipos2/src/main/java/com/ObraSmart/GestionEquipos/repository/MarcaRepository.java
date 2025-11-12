package com.ObraSmart.GestionEquipos.repository;

import com.ObraSmart.GestionEquipos.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

}
