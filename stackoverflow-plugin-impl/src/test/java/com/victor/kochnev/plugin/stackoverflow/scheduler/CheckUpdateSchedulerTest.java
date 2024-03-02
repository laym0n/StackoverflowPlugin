package com.victor.kochnev.plugin.stackoverflow.scheduler;

import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

class CheckUpdateSchedulerTest extends BaseBootTest {
    @SpyBean
    CheckUpdateScheduler checkUpdateScheduler;

    @Test
    void successExecute() {
        //Assign

        //Action
        checkUpdateScheduler.checkUpdateQuestions();

        //Assert
    }

    @Test
    void stackOverflowErrorResponse_() {
        //Assign
        // TODO stub error response

        //Action
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> Mockito.verify(checkUpdateScheduler, Mockito.times(10)).checkUpdateQuestions());

        //Assert
        // TODO ничего не обновилось
    }

    @BeforeEach
    void prepareDb() {
        
    }
}