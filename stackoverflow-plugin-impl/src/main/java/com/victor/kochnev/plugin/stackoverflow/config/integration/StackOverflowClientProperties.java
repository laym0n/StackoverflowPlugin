package com.victor.kochnev.plugin.stackoverflow.config.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integration.stackoverflow")
@Data
public class StackOverflowClientProperties {
    private String host;
    private String key;
}
