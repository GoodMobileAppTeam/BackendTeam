package mobile.backend.global.config.S3;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Properties {

    private final String bucket;
    private final Path path;

    public S3Properties(String bucket, Path path) {
        this.bucket = bucket;
        this.path = path;
    }

    @Getter
    public static class Path {
        private final String profile;
        private final String record;
        private final String bgm;

        public Path(String profile, String record, String bgm) {
            this.profile = profile;
            this.record = record;
            this.bgm = bgm;
        }

    }
}
