package com.obrasmart.mediaservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import jakarta.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "app.storage")
@Data
public class StorageProperties {
  private Path basePath = Paths.get("./storage").toAbsolutePath().normalize();

  public void setBasePath(String path) {
    // Bind String -> Path safely, allowing relative paths
    this.basePath = Paths.get(path).toAbsolutePath().normalize();
  }

  @PostConstruct
  public void ensureDirectory() {
    try {
      if (basePath != null) {
        Files.createDirectories(basePath);
      }
    } catch (Exception e) {
      throw new IllegalStateException("No se pudo crear el directorio de almacenamiento: " + basePath, e);
    }
  }
}
