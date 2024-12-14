package com.almonium.infra.storage.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.exception.FirebaseIntegrationException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE)
public class FirebaseStorageService {
    final Storage storage;

    @Value("${firebase.storage.bucket}")
    String bucketName;

    public FirebaseStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public void deleteFile(String filePath) {
        BlobId blobId = BlobId.of(bucketName, filePath);
        boolean deleted = storage.delete(blobId);

        if (!deleted) {
            throw new FirebaseIntegrationException("Failed to delete file: " + filePath + " in bucket: " + bucketName);
        }
        log.info("File deleted: {}", filePath);
    }
}
