package com.obrasmart.mediaservice.controller;

import com.obrasmart.mediaservice.dto.ByOwnersRequest;
import com.obrasmart.mediaservice.dto.MediaFileResponse;
import com.obrasmart.mediaservice.entity.MediaFile;
import com.obrasmart.mediaservice.service.MediaAppService;
import com.obrasmart.mediaservice.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class MediaController {

  private final MediaAppService service;
  private final StorageService storage;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public MediaFileResponse upload(@RequestPart("file") MultipartFile file,
                                  @RequestParam String ownerType,
                                  @RequestParam String ownerId,
                                  @RequestParam String purpose) throws Exception {
    return service.save(file, ownerType, ownerId, purpose);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> download(@PathVariable UUID id) {
    MediaFile mf = service.getEntity(id);
    Resource res = storage.load(Paths.get(mf.getPath()));
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(mf.getContentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + mf.getOriginalName() + "\"")
        .contentLength(mf.getSize())
        .body(res);
  }

  @GetMapping("/by-owner")
  public List<MediaFileResponse> byOwner(@RequestParam String ownerType,
                                         @RequestParam String ownerId,
                                         @RequestParam String purpose) {
    return service.getByOwner(ownerType, ownerId, purpose);
  }

  @PostMapping("/by-owners")
  public Map<String, MediaFileResponse> byOwners(@RequestBody ByOwnersRequest req) {
    return service.getByOwners(req.ownerType(), req.purpose(), req.ownerIds());
  }

  @GetMapping("/exists")
  public ResponseEntity<MediaFileResponse> exists(@RequestParam String ownerType,
                                                  @RequestParam String ownerId,
                                                  @RequestParam String purpose) {
    Optional<MediaFileResponse> latest = service.getLatest(ownerType, ownerId, purpose);
    return latest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
