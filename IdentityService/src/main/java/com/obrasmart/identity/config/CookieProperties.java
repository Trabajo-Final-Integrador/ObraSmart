package com.obrasmart.identity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.cookies")
public class CookieProperties {
    private String accessName = "ACCESS_TOKEN";
    private String refreshName = "REFRESH_TOKEN";
    private boolean secure = false;
    private String sameSite = "Lax";
}
