package com.ObraSmart.GestionReportes.repository;



import com.ObraSmart.GestionReportes.entity.DashboardSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardSnapshotRepository extends JpaRepository<DashboardSnapshot, Long> {

    List<DashboardSnapshot> findTop10ByOrderByFechaGeneracionDesc();
}
