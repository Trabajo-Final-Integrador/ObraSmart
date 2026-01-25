package com.obrasmart.mediaservice.repository;

import com.obrasmart.mediaservice.entity.EquipmentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EquipmentImageRepository extends JpaRepository<EquipmentImage, UUID> {
  Optional<EquipmentImage> findByEquipoId(String equipoId);
}
