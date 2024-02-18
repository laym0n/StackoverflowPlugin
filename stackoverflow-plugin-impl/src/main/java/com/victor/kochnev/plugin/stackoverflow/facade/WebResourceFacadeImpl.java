package com.victor.kochnev.plugin.stackoverflow.facade;

import com.victor.kochnev.integration.plugin.api.dto.CanObserveRequest;
import com.victor.kochnev.integration.plugin.api.dto.CanObserveResponse;
import com.victor.kochnev.integration.plugin.api.dto.WebResourceAddRequest;
import com.victor.kochnev.integration.plugin.api.dto.WebResourceDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponse;
import com.victor.kochnev.plugin.stackoverflow.api.dto.Question;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionResponseModel;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponse;
import com.victor.kochnev.plugin.stackoverflow.client.StackOverflowClient;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowInfo;
import com.victor.kochnev.plugin.stackoverflow.exception.ParseDescriptionException;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import com.victor.kochnev.plugin.stackoverflow.service.parser.ParserService;
import com.victor.kochnev.plugin.stackoverflow.service.webclient.WebClientService;
import com.victor.kochnev.plugin.stackoverflow.service.webresource.WebResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebResourceFacadeImpl implements WebResourceFacade {
    private final WebResourceService webResourceService;
    private final WebClientService webClientService;
    private final ParserService parserService;

    @Override
    public CanObserveResponse canObserve(CanObserveRequest request) {
        CanObserveResponse response = new CanObserveResponse();
        response.setIsObservable(true);
        QuestionResponseModel questionResponse = null;
        try {
            Integer questionId = parserService.parseQuestionId(request.getDescription());
            questionResponse = webClientService.getQuestionResponse(questionId);
        } catch (ParseDescriptionException | ResourceNotFoundException e) {
            log.info("Bad resource for canObserve " + ExceptionUtils.getMessage(e));
            response.setIsObservable(false);
        }
        if (Boolean.TRUE == response.getIsObservable()) {
            WebResourceDto webResourceDto = new WebResourceDto();
            webResourceDto.setName(questionResponse.get().toString());
            webResourceDto.setDescription(question.getTitle());
            response.setWebResource(webResourceDto);
        }
        return response;
    }

    @Override
    public WebResourceDto addForObserve(WebResourceAddRequest request) {
        Question question = getQuestion(request.getDescription());
        AnswersResponse answersResponse = stackOverflowClient.getAnswersResponse(question.getQuestionId());

        StackOverflowInfo entity = new StackOverflowInfo();
        entity.setQuestionId(question.getQuestionId());
        entity.setAnswersList();
        webResourceService.create();
        return null;
    }

    private Question getQuestion(String description) {
        Integer idQuestion = parseIdQuestion(description);
        QuestionsResponse questionsResponse = stackOverflowClient.getQuestionsResponse(idQuestion);
        if (questionsResponse.getItems().isEmpty()) {
            throw new ParseDescriptionException("URI not points at question");
        }
        Question question = questionsResponse.getItems().get(0);
        return question;
    }
}
