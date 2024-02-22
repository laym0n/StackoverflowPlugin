package com.victor.kochnev.plugin.stackoverflow.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.victor.kochnev.integration.plugin.api.dto.ErrorMessageDto;
import com.victor.kochnev.integration.plugin.api.dto.WebResourceAddRequest;
import com.victor.kochnev.integration.plugin.api.dto.WebResourceDto;
import com.victor.kochnev.plugin.stackoverflow.BaseControllerTest;
import com.victor.kochnev.plugin.stackoverflow.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.config.StackOverflowClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebResourceControllerAddWebResourceTest extends BaseControllerTest {
    private static final String ADD_ENDPOINT = "/webresource/add";

    @Autowired
    StackOverflowClientProperties clientProperties;

    @Test
    void testSuccessAddWebResource() {
        //Assign
        Long questionId = 3946797L;
        String title = "title";

        Long answerId = 1L;
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Long userId = 2L;
        String userName = "userName";
        var request = new WebResourceAddRequest();
        request.setDescription("https://stackoverflow.com/questions/3946797/cgaffinetransformmakescale-makes-uiview-jump-to-original-size-before-scale");
        wireMockServer.stubFor(WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                .willReturn(wireMockResponse(prepareQuestionsResponseDto(questionId, title))));
        wireMockServer.stubFor(WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                .willReturn(wireMockResponse(prepareAnswersResponseDto(answerId, creationDate.toEpochSecond(), userId, userName))));

        //Action
        MvcResult mvcResult = post(ADD_ENDPOINT, request);

        //Assert
        assertHttpStatusOk(mvcResult);
        WebResourceDto actualResponse = getResponseDto(mvcResult, WebResourceDto.class);
        assertEquals(questionId.toString(), actualResponse.getName());
        assertEquals(title, actualResponse.getDescription());

        var optionalDbQuestion = questionRepository.findByQuestionId(questionId);
        assertTrue(optionalDbQuestion.isPresent());
        var dbQuestion = optionalDbQuestion.get();

        assertEquals(questionId, dbQuestion.getQuestionId());
        assertEquals(title, dbQuestion.getTitle());

        var answersList = dbQuestion.getAnswersList();
        assertNotNull(answersList);
        assertEquals(1, answersList.size());
        var answer = answersList.get(0);

        assertEquals(answerId, answer.getAnswerId());
        assertEquals(userName, answer.getUserName());
        ZonedDateTime actualCreateDate = answer.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertTrue(creationDate.isEqual(actualCreateDate),
                () -> "Expected " + creationDate + " but get " + actualCreateDate);
        assertEquals(userId, answer.getUserId());
    }

    @Test
    void testAddWebResource_StackOverflowIntegrationException_Expect503() {
        //Assign
        Long questionId = 3946797L;
        var request = new WebResourceAddRequest();
        request.setDescription("https://stackoverflow.com/questions/3946797/cgaffinetransformmakescale-makes-uiview-jump-to-original-size-before-scale");
        wireMockServer.stubFor(WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                .willReturn(wireMockResponseJson("error-response.json").withStatus(400)));

        //Action
        MvcResult mvcResult = post(ADD_ENDPOINT, request);

        //Assert
        assertHttpStatus(mvcResult, HttpStatus.SERVICE_UNAVAILABLE);
        ErrorMessageDto errorMessageDto = getResponseDto(mvcResult, ErrorMessageDto.class);
        assertEquals("Get 400 from StackOverflow", errorMessageDto.getMessage());
    }

    @Test
    void testAddWebResource_BadRequest_Expect400() {
        //Assign
        var request = new WebResourceAddRequest();

        //Action
        MvcResult mvcResult = post(ADD_ENDPOINT, request);

        //Assert
        assertHttpStatus(mvcResult, HttpStatus.BAD_REQUEST);
    }

    QuestionsResponseDto prepareQuestionsResponseDto(Long questionId, String title) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestionId(questionId);
        questionDto.setTitle(title);

        QuestionsResponseDto response = new QuestionsResponseDto();
        response.setHasMore(false);
        response.setItems(List.of(
                questionDto
        ));
        return response;
    }

    AnswersResponseDto prepareAnswersResponseDto(Long answerId, Long creationDate, Long userId, String userName) {
        ShallowUserDto shallowUserDto = new ShallowUserDto();
        shallowUserDto.setDisplayName(userName);
        shallowUserDto.setUserId(userId);

        AnswerDto answerDto = new AnswerDto();
        answerDto.setCreationDate(creationDate);
        answerDto.setAnswerId(answerId);
        answerDto.setOwner(shallowUserDto);

        AnswersResponseDto response = new AnswersResponseDto();
        response.setHasMore(false);
        response.setItems(List.of(
                answerDto
        ));
        return response;
    }
}
