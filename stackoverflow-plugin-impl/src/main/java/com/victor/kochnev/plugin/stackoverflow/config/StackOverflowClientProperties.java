package com.victor.kochnev.plugin.stackoverflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client.stackoverflow")
@Data
public class StackOverflowClientProperties {
    private String host;
    private String key;
}
