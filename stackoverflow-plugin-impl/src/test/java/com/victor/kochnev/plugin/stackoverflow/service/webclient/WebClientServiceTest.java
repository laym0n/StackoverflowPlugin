package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.config.StackOverflowClientProperties;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class WebClientServiceTest extends BaseBootTest {
    @Autowired
    WebClientService webClientService;

    @Autowired
    StackOverflowClientProperties clientProperties;

    @Test
    void test() {
        //Assign
        Long questionId = 3946797L;
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                        .willReturn(wireMockResponseJson("question-response.json")));
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                        .willReturn(wireMockResponseJson("answers-response.json")));

        //Action
        StackOverflowQuestion stackOverflowInfo = webClientService.getStackOverflowInfo(questionId);

        //Assert
        assertNull(stackOverflowInfo.getId());
        assertNull(stackOverflowInfo.getVersion());
        assertNull(stackOverflowInfo.getCreateDate());
        assertNull(stackOverflowInfo.getLastChangeDate());

        assertEquals(questionId, stackOverflowInfo.getQuestionId());
        assertEquals("CGAffineTransformMakeScale Makes UIView Jump to Original Size before scale", stackOverflowInfo.getTitle());

        var answersList = stackOverflowInfo.getAnswersList();
        assertNotNull(answersList);
        assertEquals(3, answersList.size());

        var optionalAnswer = answersList.stream().filter(answer -> answer.getAnswerId().equals(19900155L)).findFirst();
        assertTrue(optionalAnswer.isPresent());
        var answer = optionalAnswer.get();
        assertEquals(19900155L, answer.getAnswerId());
        assertEquals(1142674L, answer.getUserId());
        assertEquals("ygweric", answer.getUserName());
        ZonedDateTime expectedLastActivityDate = ZonedDateTime.of(2013, 11, 11, 6, 28, 38, 0, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime actualLastActivityDate = answer.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertTrue(expectedLastActivityDate.isEqual(actualLastActivityDate),
                () -> "expectedLastActivityDate " + expectedLastActivityDate + " actualLastActivityDate " + actualLastActivityDate);
    }
}
