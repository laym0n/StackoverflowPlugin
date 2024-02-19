package com.victor.kochnev.plugin.stackoverflow.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswerDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;
import com.victor.kochnev.plugin.stackoverflow.config.StackOverflowClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class StackOverflowClientTest extends BaseBootTest {
    @Autowired
    StackOverflowClient stackOverflowClient;

    @Autowired
    StackOverflowClientProperties clientProperties;

    @Test
    void testGetQuestionsResponse() {
        //Assign
        Long questionId = 3946797L;
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                        .willReturn(wireMockResponseJson("question-response.json")));

        //Action
        QuestionsResponseDto actualResponse = stackOverflowClient.getQuestionsResponse(questionId);

        //Assert
        assertEquals(1, actualResponse.getItems().size());

        QuestionDto questionDto = actualResponse.getItems().get(0);
        ZonedDateTime expectedLastActivityDate = ZonedDateTime.of(2013, 11, 11, 06, 28, 38, 0, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime actualLastActivityDate = ZonedDateTime.ofInstant(Instant.ofEpochSecond(questionDto.getLastActivityDate()), ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        assertTrue(expectedLastActivityDate.isEqual(actualLastActivityDate),
                () -> "expectedLastActivityDate " + expectedLastActivityDate + " actualLastActivityDate " + actualLastActivityDate);
        assertEquals(questionId, questionDto.getQuestionId());
        assertEquals("CGAffineTransformMakeScale Makes UIView Jump to Original Size before scale", questionDto.getTitle());
        assertEquals(questionId, questionDto.getQuestionId());

        assertNotNull(questionDto.getOwner());
        assertEquals(301043L, questionDto.getOwner().getUserId());
        assertEquals("jps", questionDto.getOwner().getDisplayName());
    }

    @Test
    void testGetAnswersResponse() {
        //Assign
        Long questionId = 3946797L;
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                        .willReturn(wireMockResponseJson("answers-response.json")));

        //Action
        AnswersResponseDto actualResponse = stackOverflowClient.getAnswersResponse(questionId);

        //Assert
        assertEquals(3, actualResponse.getItems().size());

        AnswerDto answerDto = actualResponse.getItems().get(0);
        assertNotNull(answerDto);
        ZonedDateTime expectedLastActivityDate = ZonedDateTime.of(2013, 11, 11, 06, 28, 38, 0, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime actualLastActivityDate = ZonedDateTime.ofInstant(Instant.ofEpochSecond(answerDto.getLastActivityDate()), ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        assertTrue(expectedLastActivityDate.isEqual(actualLastActivityDate),
                () -> "expectedLastActivityDate " + expectedLastActivityDate + " actualLastActivityDate " + actualLastActivityDate);
        assertEquals(questionId, answerDto.getQuestionId());
        assertEquals(19900155L, answerDto.getAnswerId());

        assertNotNull(answerDto.getOwner());
        assertEquals(1142674L, answerDto.getOwner().getUserId());
        assertEquals("ygweric", answerDto.getOwner().getDisplayName());
    }
}
