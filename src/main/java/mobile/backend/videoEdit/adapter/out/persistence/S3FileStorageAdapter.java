package mobile.backend.videoEdit.adapter.out.persistence;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mobile.backend.videoEdit.application.port.out.FileStoragePort;
import mobile.backend.videoEdit.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileStorageAdapter implements FileStoragePort {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static-region}")
    private String region;

    @Override
    public String upload(byte[] data, String fileName, String directory) {
        try {
            String key = generateKey(directory, fileName);
            String contentType = determineContentType(fileName);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(data));

            String url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, region, key);

            log.info("파일 업로드 성공: {}", url);
            return url;

        } catch (Exception e) {
            log.error("S3 업로드 실패: {}", fileName, e);
            throw new FileStorageException("파일 업로드에 실패했습니다.", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
            log.info("파일 삭제 성공: {}", key);

        } catch (Exception e) {
            log.error("S3 삭제 실패: {}", fileUrl, e);
        }
    }

    private String generateKey(String directory, String fileName) {
        String extension = extractExtension(fileName);
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        return directory + "/" + uniqueFileName;
    }

    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String extractKeyFromUrl(String fileUrl) {
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, region);
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }
        throw new IllegalArgumentException("잘못된 S3 URL 형식: " + fileUrl);
    }

    private String determineContentType(String fileName) {
        if (fileName == null) return "application/octet-stream";

        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerName.endsWith(".png")) {
            return "image/png";
        } else if (lowerName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerName.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}
