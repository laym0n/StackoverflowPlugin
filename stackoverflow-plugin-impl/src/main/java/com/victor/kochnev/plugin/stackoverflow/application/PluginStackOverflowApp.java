package com.victor.kochnev.plugin.stackoverflow.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.victor.kochnev.plugin.stackoverflow.*")
public class PluginStackOverflowApp {
    public static void main(String[] args) {
        SpringApplication.run(PluginStackOverflowApp.class, args);
    }
}
