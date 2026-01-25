package com.obrasmart.mediaservice.service;

import com.obrasmart.mediaservice.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class StorageService {

  private final StorageProperties props;

  public Path store(String ownerType, String ownerId, String purpose, String fileId, String ext, InputStream in) throws IOException {
    Path target = props.getBasePath().resolve(ownerType).resolve(ownerId).resolve(purpose);
    Files.createDirectories(target);
    Path filePath = target.resolve(ext.isEmpty() ? fileId : fileId + "." + ext);
    Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
    return filePath;
  }

  public Resource load(Path path) {
    return new FileSystemResource(path);
  }
}
