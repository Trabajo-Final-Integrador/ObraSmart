package com.ObraSmart.GestionReportes.Repository;



import com.ObraSmart.GestionReportes.Entity.DashboardSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardSnapshotRepository extends JpaRepository<DashboardSnapshot, Long> {

    List<DashboardSnapshot> findTop10ByOrderByFechaGeneracionDesc();
}
