package com.victor.kochnev.plugin.stackoverflow.scheduler;

import com.victor.kochnev.plugin.stackoverflow.config.SchedulerProperties;
import com.victor.kochnev.plugin.stackoverflow.facade.WebResourceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@ConditionalOnProperty(value = "app.scheduling.enabled", havingValue = "true")
@RequiredArgsConstructor
public class CheckUpdateScheduler {
    private final WebResourceFacade webResourceFacade;
    private final SchedulerProperties schedulerProperties;

    @Scheduled(fixedDelayString = "#{@schedulerProperties.interval}")
    public void checkUpdateQuestions() {
        webResourceFacade.checkUpdateWebResources(Duration.of(schedulerProperties.getInterval(), ChronoUnit.MILLIS));
    }
}
