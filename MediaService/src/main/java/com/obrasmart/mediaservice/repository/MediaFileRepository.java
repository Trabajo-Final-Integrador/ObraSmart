package com.obrasmart.mediaservice.repository;

import com.obrasmart.mediaservice.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaFileRepository extends JpaRepository<MediaFile, UUID> {
  List<MediaFile> findByOwnerTypeAndOwnerIdAndPurposeAndIsActiveTrueOrderByCreatedAtDesc(String ownerType, String ownerId, String purpose);

  List<MediaFile> findByOwnerTypeAndPurposeAndOwnerIdInAndIsActiveTrue(String ownerType, String purpose, List<String> ownerIds);
}
