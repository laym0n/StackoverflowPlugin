package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionResponseModel;

public interface WebClientService {
    QuestionResponseModel getQuestionResponse(Integer questionId);
}
