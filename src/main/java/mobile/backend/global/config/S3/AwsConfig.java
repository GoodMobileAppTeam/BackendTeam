package mobile.backend.global.config.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AmazonProperties.class)
public class AwsConfig {

    private final AmazonProperties amazonProperties;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
            amazonProperties.getCredentials().getAccessKey(),
            amazonProperties.getCredentials().getSecretKey()
        );
        return StaticCredentialsProvider.create(awsCredentials);
    }
    
    @Bean
    public S3Client s3Client(AwsCredentialsProvider provider) {
        return S3Client.builder()
            .region(Region.of(amazonProperties.getRegion().getStaticRegion()))
            .credentialsProvider(provider)
            .build();
    }
}
