package com.victor.kochnev.plugin.stackoverflow.controller;

import com.victor.kochnev.platform.api.dto.WebResourceRemoveRequest;
import com.victor.kochnev.plugin.stackoverflow.BaseControllerTest;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowQuestionBuilder;
import com.victor.kochnev.plugin.stackoverflow.config.StackOverflowClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertFalse;

class WebResourceControllerRemoveTest extends BaseControllerTest {
    private static final String WEBRESOURCE_REMOVE_ENDPOINT = "/webresource";

    @Autowired
    StackOverflowClientProperties clientProperties;

    @Test
    void testSuccessRemove() {
        //Assign
        questionRepository.save(StackOverflowQuestionBuilder.defaultPersistedBuilder().build());
        Long questionId = StackOverflowQuestionBuilder.DEFAULT_QUESTION_ID;
        WebResourceRemoveRequest request = new WebResourceRemoveRequest();
        request.setName(questionId.toString());

        //Action
        MvcResult mvcResult = delete(WEBRESOURCE_REMOVE_ENDPOINT, request);

        //Assert
        assertHttpStatusOk(mvcResult);

        var optionalDbQuestion = questionRepository.findByQuestionId(questionId);
        assertFalse(optionalDbQuestion.isPresent());
    }

    @Test
    void testRemoveNotExistedWebResources_Expect404() {
        //Assign
        Long questionId = StackOverflowQuestionBuilder.DEFAULT_QUESTION_ID;
        WebResourceRemoveRequest request = new WebResourceRemoveRequest();
        request.setName(questionId.toString());

        //Action
        MvcResult mvcResult = delete(WEBRESOURCE_REMOVE_ENDPOINT, request);

        //Assert
        assertHttpStatus(mvcResult, HttpStatus.NOT_FOUND);

        var optionalDbQuestion = questionRepository.findByQuestionId(questionId);
        assertFalse(optionalDbQuestion.isPresent());
    }
}
