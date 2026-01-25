package com.ObraSmart.GestionLogistica.repository;

import com.ObraSmart.GestionLogistica.entity.TrasladoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrasladoRepository extends JpaRepository<TrasladoEquipo, Long> {
}
