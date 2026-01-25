package com.obrasmart.mediaservice.service;

import com.obrasmart.mediaservice.dto.MediaFileResponse;
import com.obrasmart.mediaservice.entity.MediaFile;
import com.obrasmart.mediaservice.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaAppService {

  private static final long MAX_IMAGE = 5 * 1024 * 1024;
  private static final long MAX_PDF = 20 * 1024 * 1024;
  private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
  private static final Set<String> PDF_TYPES = Set.of("application/pdf");
  private static final Set<String> UNIQUE_PURPOSES = Set.of("FOTO", "MANUAL", "PERFIL", "CARNET");

  private final MediaFileRepository repo;
  private final StorageService storage;

  @Transactional
  public MediaFileResponse save(MultipartFile file, String ownerType, String ownerId, String purpose) throws IOException {
    validate(file);
    String ext = extensionOf(file.getOriginalFilename());
    UUID id = UUID.randomUUID();

    if (isUniquePurpose(purpose)) {
      deactivatePrevious(ownerType, ownerId, purpose);
    }

    Path stored = storage.store(ownerType, ownerId, purpose, id.toString(), ext, file.getInputStream());

    MediaFile mf = new MediaFile();
    mf.setId(id);
    mf.setOwnerType(ownerType);
    mf.setOwnerId(ownerId);
    mf.setPurpose(purpose);
    mf.setOriginalName(file.getOriginalFilename());
    mf.setContentType(Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"));
    mf.setSize(file.getSize());
    mf.setPath(stored.toString());
    mf.setIsActive(true);
    return toResponse(repo.save(mf));
  }

  @Transactional(readOnly = true)
  public MediaFile getEntity(UUID id) {
    return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public MediaFileResponse get(UUID id) {
    return toResponse(getEntity(id));
  }

  @Transactional(readOnly = true)
  public List<MediaFileResponse> getByOwner(String ownerType, String ownerId, String purpose) {
    return repo.findByOwnerTypeAndOwnerIdAndPurposeAndIsActiveTrueOrderByCreatedAtDesc(ownerType, ownerId, purpose)
        .stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public Optional<MediaFileResponse> getLatest(String ownerType, String ownerId, String purpose) {
    return repo.findByOwnerTypeAndOwnerIdAndPurposeAndIsActiveTrueOrderByCreatedAtDesc(ownerType, ownerId, purpose)
        .stream().findFirst().map(this::toResponse);
  }

  @Transactional(readOnly = true)
  public Map<String, MediaFileResponse> getByOwners(String ownerType, String purpose, List<String> ownerIds) {
    return repo.findByOwnerTypeAndPurposeAndOwnerIdInAndIsActiveTrue(ownerType, purpose, ownerIds)
        .stream()
        .collect(Collectors.toMap(MediaFile::getOwnerId, this::toResponse,
            (a, b) -> a.createdAt().isAfter(b.createdAt()) ? a : b));
  }

  @Transactional
  public void delete(UUID id) {
    MediaFile mf = getEntity(id);
    mf.setIsActive(false);
    repo.save(mf);
  }

  private void validate(MultipartFile file) {
    String ct = Optional.ofNullable(file.getContentType()).orElse("").toLowerCase(Locale.ROOT);
    long size = file.getSize();
    if (IMAGE_TYPES.contains(ct)) {
      if (size > MAX_IMAGE) throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Imagen >5MB");
    } else if (PDF_TYPES.contains(ct)) {
      if (size > MAX_PDF) throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "PDF >20MB");
    } else {
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Tipo no permitido");
    }
  }

  private boolean isUniquePurpose(String purpose) {
    return UNIQUE_PURPOSES.contains(Optional.ofNullable(purpose).orElse("").toUpperCase(Locale.ROOT));
  }

  private void deactivatePrevious(String ownerType, String ownerId, String purpose) {
    List<MediaFile> prev = repo.findByOwnerTypeAndOwnerIdAndPurposeAndIsActiveTrueOrderByCreatedAtDesc(ownerType, ownerId, purpose);
    prev.forEach(m -> m.setIsActive(false));
    repo.saveAll(prev);
  }

  private String extensionOf(String name) {
    if (name == null) return "";
    int idx = name.lastIndexOf('.');
    if (idx < 0 || idx == name.length() - 1) return "";
    return name.substring(idx + 1);
  }

  private MediaFileResponse toResponse(MediaFile mf) {
    return new MediaFileResponse(
        mf.getId(),
        mf.getOwnerType(),
        mf.getOwnerId(),
        mf.getPurpose(),
        mf.getOriginalName(),
        mf.getContentType(),
        mf.getSize(),
        mf.getCreatedAt()
    );
  }
}
