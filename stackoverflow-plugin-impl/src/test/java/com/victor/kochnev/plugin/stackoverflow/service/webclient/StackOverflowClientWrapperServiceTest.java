package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.config.StackOverflowClientProperties;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StackOverflowClientWrapperServiceTest extends BaseBootTest {
    @Autowired
    StackOverflowClientWrapperService stackOverflowClientWrapperService;

    @Autowired
    StackOverflowClientProperties clientProperties;

    @Test
    void testGetStackOverflowInfo() {
        //Assign
        Long questionId = 3946797L;
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                        .willReturn(wireMockResponseJson("question-response.json")));
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                        .willReturn(wireMockResponseJson("answers-response.json")));

        //Action
        StackOverflowQuestion stackOverflowInfo = stackOverflowClientWrapperService.getStackOverflowInfo(questionId);

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

    @Test
    void testFindAllByQuestionIds() {
        //Assign
        List<Long> questionIdsList = List.of(3946797L, 285177L, 49887953L);
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", "3946797;285177;49887953", clientProperties.getKey(), 1))
                        .inScenario("QuestionsScenario")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(wireMockResponseJson("question-response-page1.json"))
                        .willSetStateTo("SECOND RESPONSE"));
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", "3946797;285177;49887953", clientProperties.getKey(), 2))
                        .inScenario("QuestionsScenario")
                        .whenScenarioStateIs("SECOND RESPONSE")
                        .willReturn(wireMockResponseJson("question-response-page2.json")));
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", "3946797;285177;49887953", clientProperties.getKey(), 1))
                        .inScenario("AnswersScenario")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(wireMockResponseJson("answers-response-page1.json"))
                        .willSetStateTo("SECOND RESPONSE"));
        wireMockServer.stubFor(
                WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", "3946797;285177;49887953", clientProperties.getKey(), 2))
                        .inScenario("AnswersScenario")
                        .whenScenarioStateIs("SECOND RESPONSE")
                        .willReturn(wireMockResponseJson("answers-response-page2.json")));

        //Action
        List<StackOverflowQuestion> stackOverflowQuestionList = stackOverflowClientWrapperService.findAllByQuestionIds(questionIdsList);

        //Assert
        assertNotNull(stackOverflowQuestionList);
        assertEquals(3, stackOverflowQuestionList.size());

        var optionalFirst = stackOverflowQuestionList.stream().filter(question -> question.getQuestionId().equals(3946797L)).findFirst();
        assertTrue(optionalFirst.isPresent());
        StackOverflowQuestion first = optionalFirst.get();
        assertNotNull(first.getAnswersList());
        assertEquals(3, first.getAnswersList().size());

        var optionalSecond = stackOverflowQuestionList.stream().filter(question -> question.getQuestionId().equals(285177L)).findFirst();
        assertTrue(optionalSecond.isPresent());
        StackOverflowQuestion second = optionalSecond.get();
        assertNotNull(second.getAnswersList());
        assertEquals(22, second.getAnswersList().size());

        var optionalThird = stackOverflowQuestionList.stream().filter(question -> question.getQuestionId().equals(49887953L)).findFirst();
        assertTrue(optionalThird.isPresent());
        StackOverflowQuestion third = optionalThird.get();
        assertNotNull(third.getAnswersList());
        assertEquals(4, third.getAnswersList().size());
    }
}
