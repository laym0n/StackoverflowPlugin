package com.victor.kochnev.plugin.stackoverflow.config.integration;

import com.victor.kochnev.platform.client.NotificationClient;
import com.victor.kochnev.platform.client.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CheckUpdatePlatformConfigurationProperties.class)
@RequiredArgsConstructor
public class CheckUpdatePlatformConfiguration {
    private final CheckUpdatePlatformConfigurationProperties properties;

    @Bean
    public NotificationClient notificationClient() {
        return new NotificationClient(platformApiClient());
    }

    @Bean
    public ApiClient platformApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(properties.getBasePath());
        apiClient.setUsername(properties.getUsername());
        apiClient.setPassword(properties.getPassword());
        return apiClient;
    }
}
