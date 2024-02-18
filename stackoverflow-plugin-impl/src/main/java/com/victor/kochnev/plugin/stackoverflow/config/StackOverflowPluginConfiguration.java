package com.victor.kochnev.plugin.stackoverflow.config;

import com.victor.kochnev.plugin.stackoverflow.ComponentScanMarker;
import com.victor.kochnev.plugin.stackoverflow.entity.EntityScanMarker;
import com.victor.kochnev.plugin.stackoverflow.repository.RepositoryScanMarker;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableConfigurationProperties(StackOverflowClientProperties.class)
@ComponentScan(basePackageClasses = ComponentScanMarker.class)
@EntityScan(basePackageClasses = EntityScanMarker.class)
@EnableJpaRepositories(basePackageClasses = RepositoryScanMarker.class)
public class StackOverflowPluginConfiguration {
}
