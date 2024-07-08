package t3h.bigproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPIConfig() {
        return new OpenAPI().info(buildInfo());
    }

    private Info buildInfo() {
        return new Info()
                .title("WTS-Config Shipper Preferences API")
                .description("Shipper Preference API to create rules");
    }
}
