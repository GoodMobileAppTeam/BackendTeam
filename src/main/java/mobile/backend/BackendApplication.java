package mobile.backend;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import mobile.backend.global.config.S3.AmazonProperties;
import mobile.backend.global.config.S3.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = { S3AutoConfiguration.class })
@EnableConfigurationProperties({AmazonProperties.class, S3Properties.class})
public class BackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

}
