package com.victor.kochnev.plugin.stackoverflow.scheduler;

import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.facade.WebResourceFacade;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@TestPropertySource(properties = {
        "app.scheduling.enabled=true",
        "app.scheduling.interval=100"
})
class CheckUpdateSchedulerTest extends BaseBootTest {
    @SpyBean
    CheckUpdateScheduler checkUpdateScheduler;
    @MockBean
    WebResourceFacade webResourceFacade;

    @SneakyThrows
    @Test
    void successExecute() {
        await()
                .atMost(600, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> Mockito.verify(checkUpdateScheduler, Mockito.times(6)).checkUpdateQuestions());
    }
}
