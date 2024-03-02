package com.victor.kochnev.plugin.stackoverflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "check.update")
@Data
@Component
public class SchedulerProperties {
    private long interval = 5L * 60L * 1000L;
}
