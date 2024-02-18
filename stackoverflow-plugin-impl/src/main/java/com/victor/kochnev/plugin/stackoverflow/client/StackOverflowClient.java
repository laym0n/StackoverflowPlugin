package com.victor.kochnev.plugin.stackoverflow.client;

import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionResponseModel;

public interface StackOverflowClient {
    QuestionResponseModel getQuestionsResponse(Integer idQuestion);
}
