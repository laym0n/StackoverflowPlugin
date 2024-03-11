package com.victor.kochnev.plugin.stackoverflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.scheduling")
@Data
@Component
public class SchedulerProperties {
    private boolean enabled;
    private long interval = 5L * 60L * 1000L;
}
