package mobile.backend.global.config.S3;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cloud.aws")
public class AmazonProperties {

    private final Credentials credentials;
    private final Region region;

    public AmazonProperties(Credentials credentials, Region region) {
        this.credentials = credentials;
        this.region = region;
    }

    @Getter
    public static class Credentials {
        private final String accessKey;
        private final String secretKey;

        public Credentials(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }
    }

    @Getter
    public static class Region {
        private final String staticRegion;

        public Region(String staticRegion) {
            this.staticRegion = staticRegion;
        }
    }
}
