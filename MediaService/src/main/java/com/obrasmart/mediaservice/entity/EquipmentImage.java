package com.obrasmart.mediaservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "equipment_images", uniqueConstraints = @UniqueConstraint(columnNames = "equipo_id"))
@Getter
@Setter
public class EquipmentImage {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "equipo_id", nullable = false, unique = true)
  private String equipoId;

  @Column(name = "source_type", nullable = false)
  private String sourceType; // LOCAL | URL

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "content_type")
  private String contentType;

  @Column(name = "size")
  private Long size;

  @Column(name = "relative_path")
  private String relativePath;

  @Column(name = "external_url")
  private String externalUrl;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();
}
