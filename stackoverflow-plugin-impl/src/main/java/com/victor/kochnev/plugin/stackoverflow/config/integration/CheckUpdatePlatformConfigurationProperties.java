package com.victor.kochnev.plugin.stackoverflow.config.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integration.platform")
@Data
public class CheckUpdatePlatformConfigurationProperties {
    private String basePath;
    private String username;
    private String password;
}
