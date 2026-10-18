package com.almonium.learning.lexicon.audio;

import java.net.URI;
import java.util.Optional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Clips in an S3-compatible bucket, Cloudflare R2 in practice: path-style addressing, and checksums only where the
 * protocol demands them, since R2 rejects the CRC trailers the SDK would otherwise add to every upload. One bucket
 * is shared by staging and production; a key is the same clip in both, so sharing costs nothing.
 */
public class S3AudioStore implements AudioStore {
    private static final int NOT_FOUND = 404;

    private final S3Client client;
    private final String bucket;

    public S3AudioStore(S3Client client, String bucket) {
        this.client = client;
        this.bucket = bucket;
    }

    public static S3AudioStore connect(
            String endpoint, String region, String bucket, String accessKeyId, String secretAccessKey) {
        S3Client client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .forcePathStyle(true)
                .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                .responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED)
                .build();
        return new S3AudioStore(client, bucket);
    }

    @Override
    public boolean exists(String key) {
        try {
            client.headObject(request -> request.bucket(bucket).key(key));
            return true;
        } catch (S3Exception exception) {
            if (exception.statusCode() == NOT_FOUND) {
                return false;
            }
            throw exception;
        }
    }

    @Override
    public void putIfAbsent(String key, byte[] bytes, String contentType) {
        if (exists(key)) {
            return;
        }
        client.putObject(
                request -> request.bucket(bucket).key(key).contentType(contentType), RequestBody.fromBytes(bytes));
    }

    @Override
    public Optional<StoredAudio> get(String key) {
        try {
            ResponseBytes<GetObjectResponse> object =
                    client.getObjectAsBytes(request -> request.bucket(bucket).key(key));
            return Optional.of(
                    new StoredAudio(object.asByteArray(), object.response().contentType()));
        } catch (S3Exception exception) {
            if (exception.statusCode() == NOT_FOUND) {
                return Optional.empty();
            }
            throw exception;
        }
    }
}
