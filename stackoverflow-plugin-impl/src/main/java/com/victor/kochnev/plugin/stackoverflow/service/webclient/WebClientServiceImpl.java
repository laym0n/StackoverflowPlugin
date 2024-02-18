package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionResponseModel;
import com.victor.kochnev.plugin.stackoverflow.client.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientServiceImpl implements WebClientService {
    private final StackOverflowClient stackOverflowClient;

    @Override
    public QuestionResponseModel getQuestionResponse(Integer questionId) {
        QuestionResponseModel questionResponse = stackOverflowClient.getQuestionsResponse(questionId);
        return questionResponse;
    }
}
