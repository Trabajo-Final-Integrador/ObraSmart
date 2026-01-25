package com.obrasmart.identity.service.impl;

import com.obrasmart.identity.config.StorageProperties;
import com.obrasmart.identity.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.InputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class StorageServiceMinio implements StorageService {

    private final StorageProperties props;
    private S3Client client() {
        return S3Client.builder()
                .endpointOverride(URI.create(props.getEndpoint()))
                .region(Region.of(props.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())))
                .forcePathStyle(true)
                .build();
    }

    @Override
    public void putObject(String key, InputStream stream, String contentType, long sizeBytes) {
        try (S3Client c = client()) {
            c.putObject(PutObjectRequest.builder()
                            .bucket(props.getBucket())
                            .key(key)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromInputStream(stream, sizeBytes));
        } catch (Exception e) {
            throw new RuntimeException("Error guardando archivo", e);
        }
    }

    @Override
    public InputStream getObject(String key) {
        try {
            S3Client c = client();
            ResponseInputStream<?> resp = c.getObject(GetObjectRequest.builder()
                    .bucket(props.getBucket())
                    .key(key)
                    .build());
            return resp;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo archivo", e);
        }
    }

    @Override
    public void deleteObject(String key) {
        try (S3Client c = client()) {
            c.deleteObject(DeleteObjectRequest.builder()
                    .bucket(props.getBucket())
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Error borrando archivo", e);
        }
    }
}
