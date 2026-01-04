package mobile.backend.global.adapter.out.s3;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.backend.global.config.S3.S3Properties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final S3Client s3Client;

    private final S3Properties s3Properties;

    public String uploadFile(String keyName, MultipartFile file) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(keyName)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(putRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return getPublicUrl(keyName);
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile", e);
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }
    }

    public void deleteObjectByUrl(String url) {
        try {
            String bucket = s3Properties.getBucket();

            String key = extractKeyFromUrl(url);

            if (key == null || key.isBlank()) {
                log.warn("S3 삭제 실패: key 추출 불가 (url: {})", url);
                return;
            }
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

            s3Client.deleteObject(request);
        } catch (Exception e) {
            log.error("S3 객체 삭제 중 오류 발생: {}", url, e);
        }
    }

    /**
     * prefix 하위 모든 객체 key 조회
     * 예: prefix = "bgm/"
     */
    public List<String> listKeys(String prefix) {
        List<String> keys = new ArrayList<>();

        String continuationToken = null;
        do {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(s3Properties.getBucket())
                .prefix(prefix)
                .continuationToken(continuationToken)
                .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            for (S3Object obj : response.contents()) {
                keys.add(obj.key());
            }

            continuationToken = response.nextContinuationToken();
        } while (continuationToken != null);

        return keys;
    }

    /**
     * S3 key → public URL
     */
    public String getPublicUrl(String key) {
        return s3Client.utilities()
            .getUrl(GetUrlRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key)
                .build())
            .toExternalForm();
    }


    private String extractKeyFromUrl(String fileUrl) {
        try {
            URI uri = URI.create(fileUrl);
            String path = uri.getPath(); // "/community/uuid123"
            if (path == null || path.isBlank()) {
                return null;
            }

            // 앞의 "/" 제거 후 반환
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            log.error("S3 URL 파싱 오류: {}", fileUrl, e);
            return null;
        }
    }
}
