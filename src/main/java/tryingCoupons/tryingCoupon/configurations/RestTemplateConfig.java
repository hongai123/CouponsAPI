package tryingCoupons.tryingCoupon.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
/**
 * Configure of RestTemplate
 */
public class RestTemplateConfig {
    String token;

    /**
     * Configuration of resTemplate bean
     * @param builder - and RestTemplateBuilder object
     * @return - returns a restTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

}
