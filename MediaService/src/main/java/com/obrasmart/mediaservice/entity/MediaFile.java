package com.obrasmart.mediaservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "media_files")
@Getter
@Setter
public class MediaFile {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String ownerType;

  @Column(nullable = false)
  private String ownerId;

  @Column(nullable = false)
  private String purpose;

  @Column(nullable = false)
  private String originalName;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private long size;

  @Column(nullable = false)
  private String path;

  @Column(nullable = false)
  private Boolean isActive = true;

  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();
}
