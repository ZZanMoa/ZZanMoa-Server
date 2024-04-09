package zzandori.zzanmoa.openapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OpenAPIConfig {

    @Value("${openapi.authentication.key}")
    private String key;
}
