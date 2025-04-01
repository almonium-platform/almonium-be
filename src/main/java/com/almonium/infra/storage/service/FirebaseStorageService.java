package com.almonium.infra.storage.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.GoogleProperties;
import com.almonium.user.core.exception.FirebaseIntegrationException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FirebaseStorageService {
    private static final String TOKEN_METADATA_KEY = "firebaseStorageDownloadTokens";
    private static final String URL_FORMAT = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s";
    private static final String BOOK_CONTENT_PATH_TEMPLATE = "books/%s.txt";

    GoogleProperties googleProperties;
    Storage storage;

    public FirebaseStorageService(GoogleProperties googleProperties) {
        this.googleProperties = googleProperties;
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String upload(byte[] fileData, String contentType, String filePath) {
        String bucketName = googleProperties.getFirebase().getStorage().getBucket();

        String token = UUID.randomUUID().toString();

        BlobId blobId = BlobId.of(bucketName, filePath);

        Map<String, String> metadata = new HashMap<>();
        metadata.put(TOKEN_METADATA_KEY, token);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .setMetadata(metadata)
                .build();

        try {
            storage.create(blobInfo, fileData);

            log.info("File uploaded to bucket {} at path: {}", bucketName, filePath);

            return String.format(URL_FORMAT, bucketName, URLEncoder.encode(filePath, StandardCharsets.UTF_8), token);

        } catch (StorageException e) {
            throw new FirebaseIntegrationException("Failed to upload file to Firebase Storage", e);
        }
    }

    public void deleteFile(String filePath) {
        String bucketName = googleProperties.getFirebase().getStorage().getBucket();

        BlobId blobId = BlobId.of(bucketName, filePath);
        boolean deleted = storage.delete(blobId);

        if (!deleted) {
            throw new FirebaseIntegrationException("Failed to delete file: " + filePath + " in bucket: " + bucketName);
        }
        log.info("File deleted: {}", filePath);
    }
    /**
     * Downloads the content of a book file based on its ID.
     * Assumes files are stored as 'books/{bookId}.txt'.
     *
     * @param bookId The ID of the book.
     * @return Byte array of the file content.
     * @throws FirebaseIntegrationException if the file is not found or another storage error occurs.
     */
    public byte[] getText(Long bookId) {
        String bucketName = googleProperties.getFirebase().getStorage().getBucket();
        String filePath = BOOK_CONTENT_PATH_TEMPLATE.formatted(bookId);
        BlobId blobId = BlobId.of(bucketName, filePath);

        log.debug("Attempting to download text content from: {}", filePath);

        try {
            byte[] content = storage.readAllBytes(blobId);
            log.info("Successfully downloaded text content from: {}, size: {} bytes", filePath, content.length);
            return content;
        } catch (StorageException e) {
            if (e.getCode() == 404) {
                log.warn("Book text file not found at path: {}", filePath);
                throw new FirebaseIntegrationException("Book text file not found: " + filePath, e);
            } else {
                log.error("Failed to download text content from Firebase Storage: {}", filePath, e);
                throw new FirebaseIntegrationException("Failed to download text content from Firebase Storage", e);
            }
        }
    }
}
