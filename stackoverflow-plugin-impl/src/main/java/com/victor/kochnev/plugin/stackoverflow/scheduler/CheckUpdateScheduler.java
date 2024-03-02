package com.victor.kochnev.plugin.stackoverflow.scheduler;

import com.victor.kochnev.plugin.stackoverflow.config.SchedulerProperties;
import com.victor.kochnev.plugin.stackoverflow.facade.WebResourceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class CheckUpdateScheduler {
    private final WebResourceFacade webResourceFacade;
    private final SchedulerProperties schedulerProperties;

    @Scheduled(fixedDelayString = "#{@schedulerProperties.interval}")
    public void checkUpdateQuestions() {
        webResourceFacade.checkUpdateWebResources(Duration.of(schedulerProperties.getInterval(), ChronoUnit.MILLIS));
    }
}
