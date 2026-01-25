package com.obrasmart.mediaservice.controller;

import com.obrasmart.mediaservice.dto.EquipmentImageResponse;
import com.obrasmart.mediaservice.entity.EquipmentImage;
import com.obrasmart.mediaservice.service.EquipmentImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping({"/api/media/equipos", "/media/equipos"})
@RequiredArgsConstructor
public class EquipmentImageController {

  private final EquipmentImageService service;

  @PostMapping(
      path = {"/{equipoId}/image", "/{equipoId}/image/upload"},
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public EquipmentImageResponse upload(@PathVariable("equipoId") String equipoId,
                                       @RequestPart("file") MultipartFile file) throws Exception {
    return service.upload(equipoId, file);
  }

  @PostMapping(
      path = {"/{equipoId}/image-link", "/{equipoId}/image/link"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public EquipmentImageResponse setLink(@PathVariable("equipoId") String equipoId,
                                        @RequestBody LinkRequest request) {
    return service.setExternalUrl(equipoId, request.url());
  }

  @GetMapping("/{equipoId}/image")
  public ResponseEntity<?> getImage(@PathVariable("equipoId") String equipoId) {
    Optional<EquipmentImage> opt = service.find(equipoId);
    if (opt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    EquipmentImage entity = opt.get();
    if ("URL".equalsIgnoreCase(entity.getSourceType()) && entity.getExternalUrl() != null) {
      return ResponseEntity.status(302).header(HttpHeaders.LOCATION, entity.getExternalUrl()).build();
    }
    if ("LOCAL".equalsIgnoreCase(entity.getSourceType()) && entity.getRelativePath() != null) {
      Path filePath = service.resolveLocalPath(entity);
      if (!Files.exists(filePath)) {
        return ResponseEntity.notFound().build();
      }
      Resource res = new FileSystemResource(filePath);
      String ct = entity.getContentType() != null ? entity.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(ct))
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + (entity.getFileName() != null ? entity.getFileName() : filePath.getFileName().toString()) + "\"")
          .body(res);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/{equipoId}/image/meta")
  public ResponseEntity<EquipmentImageResponse> getMeta(@PathVariable String equipoId) {
    try {
      return ResponseEntity.ok(service.getMeta(equipoId));
    } catch (Exception ex) {
      return ResponseEntity.notFound().build();
    }
  }

  public record LinkRequest(String url) {}
}
