package com.victor.kochnev.plugin.stackoverflow.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.victor.kochnev.platform.api.dto.CanObserveRequest;
import com.victor.kochnev.platform.api.dto.CanObserveResponse;
import com.victor.kochnev.platform.api.dto.ErrorMessageDto;
import com.victor.kochnev.platform.api.dto.WebResourceDto;
import com.victor.kochnev.plugin.stackoverflow.BaseControllerTest;
import com.victor.kochnev.plugin.stackoverflow.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.config.integration.StackOverflowClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebResourceControllerCanObserveTest extends BaseControllerTest {
    private static final String CAN_OBSERVE_ENDPOINT = "/webresource/can/observe";

    @Autowired
    StackOverflowClientProperties clientProperties;

    @Test
    void testSuccessCanObserve() {
        //Assign
        Long questionId = 3946797L;
        String title = "title";

        Long answerId = 1L;
        ZonedDateTime creationDate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Long userId = 2L;
        String userName = "userName";
        var request = new CanObserveRequest();
        request.setDescription("https://stackoverflow.com/questions/3946797/cgaffinetransformmakescale-makes-uiview-jump-to-original-size-before-scale");
        wireMockServer.stubFor(WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                .willReturn(wireMockResponse(prepareQuestionsResponseDto(questionId, title))));
        wireMockServer.stubFor(WireMock.get(String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                .willReturn(wireMockResponse(prepareAnswersResponseDto(answerId, creationDate.toEpochSecond(), userId, userName))));

        //Action
        MvcResult mvcResult = post(CAN_OBSERVE_ENDPOINT, request);

        //Assert
        assertHttpStatusOk(mvcResult);
        CanObserveResponse actualResponse = getResponseDto(mvcResult, CanObserveResponse.class);
        assertTrue(actualResponse.getIsObservable());

        WebResourceDto webResource = actualResponse.getWebResource();
        assertNotNull(webResource);
        assertEquals(questionId.toString(), webResource.getName());
        assertEquals(title, webResource.getDescriptionHeader());

        var optionalDbQuestion = questionRepository.findByQuestionId(questionId);
        assertFalse(optionalDbQuestion.isPresent());
    }

    @Test
    void testCanObserve_StackOverflowIntegrationException_Expect503() {
        //Assign
        Long questionId = 3946797L;
        var request = new CanObserveRequest();
        request.setDescription("https://stackoverflow.com/questions/3946797/cgaffinetransformmakescale-makes-uiview-jump-to-original-size-before-scale");
        wireMockServer.stubFor(WireMock.get(String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", questionId, clientProperties.getKey()))
                .willReturn(wireMockResponseJson("error-response.json").withStatus(400)));

        //Action
        MvcResult mvcResult = post(CAN_OBSERVE_ENDPOINT, request);

        //Assert
        assertHttpStatus(mvcResult, HttpStatus.SERVICE_UNAVAILABLE);
        ErrorMessageDto errorMessageDto = getResponseDto(mvcResult, ErrorMessageDto.class);
        assertEquals("Get 400 from StackOverflow", errorMessageDto.getMessage());
    }

    @Test
    void testCanObserve_BadRequest_Expect400() {
        //Assign
        var request = new CanObserveRequest();

        //Action
        MvcResult mvcResult = post(CAN_OBSERVE_ENDPOINT, request);

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
