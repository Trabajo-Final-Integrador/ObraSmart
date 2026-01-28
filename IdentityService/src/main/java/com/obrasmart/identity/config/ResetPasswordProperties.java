package com.obrasmart.identity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.reset")
public class ResetPasswordProperties {
    private String frontendBaseUrl = "http://localhost:4200";
    private String fromEmail = "no-reply@obrasmart.local";
    private String fromName = "Soporte ObraSmart";
    private long tokenTtlMin = 30;
}
