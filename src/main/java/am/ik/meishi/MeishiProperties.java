package am.ik.meishi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("meishi")
@Data
public class MeishiProperties {
    private String bucketName;
}
