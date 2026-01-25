package com.obrasmart.mediaservice.service;

import com.obrasmart.mediaservice.config.StorageProperties;
import com.obrasmart.mediaservice.dto.EquipmentImageResponse;
import com.obrasmart.mediaservice.entity.EquipmentImage;
import com.obrasmart.mediaservice.repository.EquipmentImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipmentImageService {

  private final EquipmentImageRepository repository;
  private final StorageProperties storageProperties;

  private static final String LOCAL = "LOCAL";
  private static final String URL = "URL";

  @Transactional
  public EquipmentImageResponse upload(String equipoId, MultipartFile file) throws IOException {
    validateFile(file);
    String ext = extensionOf(file.getOriginalFilename());
    String fileId = UUID.randomUUID().toString();
    Path baseDir = resolveBaseDir();
    Path targetDir = baseDir.resolve("mediaFiles").resolve("equipos").resolve(equipoId);
    Files.createDirectories(targetDir);
    Path filePath = targetDir.resolve(fileId + (ext.isEmpty() ? "" : "." + ext));
    try (InputStream in = file.getInputStream()) {
      Files.copy(in, filePath);
    }

    EquipmentImage entity = repository.findByEquipoId(equipoId).orElseGet(EquipmentImage::new);
    entity.setEquipoId(equipoId);
    entity.setSourceType(LOCAL);
    entity.setFileName(file.getOriginalFilename());
    entity.setContentType(Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"));
    entity.setSize(file.getSize());
    entity.setRelativePath(relativePath(baseDir, filePath));
    entity.setExternalUrl(null);
    entity.setUpdatedAt(Instant.now());
    repository.save(entity);

    return toResponse(entity);
  }

  @Transactional
  public EquipmentImageResponse setExternalUrl(String equipoId, String url) {
    validateUrl(url);
    EquipmentImage entity = repository.findByEquipoId(equipoId).orElseGet(EquipmentImage::new);
    entity.setEquipoId(equipoId);
    entity.setSourceType(URL);
    entity.setExternalUrl(url);
    entity.setFileName(null);
    entity.setContentType(null);
    entity.setSize(null);
    entity.setRelativePath(null);
    entity.setUpdatedAt(Instant.now());
    repository.save(entity);
    return toResponse(entity);
  }

  @Transactional(readOnly = true)
  public EquipmentImageResponse getMeta(String equipoId) {
    EquipmentImage entity = repository.findByEquipoId(equipoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return toResponse(entity);
  }

  @Transactional(readOnly = true)
  public Optional<EquipmentImage> find(String equipoId) {
    return repository.findByEquipoId(equipoId);
  }

  @Transactional(readOnly = true)
  public Path resolveLocalPath(EquipmentImage entity) {
    if (!LOCAL.equalsIgnoreCase(entity.getSourceType()) || entity.getRelativePath() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    Path baseDir = resolveBaseDir();
    return baseDir.resolve(entity.getRelativePath()).normalize();
  }

  private void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo requerido");
    }
    String ct = Optional.ofNullable(file.getContentType()).orElse("").toLowerCase();
    if (!(ct.contains("jpeg") || ct.contains("png") || ct.contains("webp") || ct.contains("jpg"))) {
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Solo jpg/png/webp");
    }
    if (file.getSize() > 5 * 1024 * 1024) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Archivo >5MB");
    }
  }

  private void validateUrl(String url) {
    if (!StringUtils.hasText(url)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL requerida");
    }
    try {
      URI uri = new URI(url);
      if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL debe ser http/https");
      }
    } catch (URISyntaxException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL inválida");
    }
  }

  private String extensionOf(String name) {
    if (name == null) return "";
    int idx = name.lastIndexOf('.');
    if (idx < 0 || idx == name.length() - 1) return "";
    return name.substring(idx + 1);
  }

  private Path resolveBaseDir() {
    Path configured = Paths.get(storageProperties.getBasePath().toString()).toAbsolutePath().normalize();
    return configured;
  }

  private String relativePath(Path baseDir, Path filePath) {
    return baseDir.relativize(filePath.toAbsolutePath()).toString().replace("\\", "/");
  }

  private EquipmentImageResponse toResponse(EquipmentImage entity) {
    String url = "/media/equipos/" + entity.getEquipoId() + "/image";
    return new EquipmentImageResponse(
        entity.getEquipoId(),
        entity.getSourceType(),
        url,
        entity.getFileName(),
        entity.getContentType(),
        entity.getSize(),
        entity.getUpdatedAt()
    );
  }
}
