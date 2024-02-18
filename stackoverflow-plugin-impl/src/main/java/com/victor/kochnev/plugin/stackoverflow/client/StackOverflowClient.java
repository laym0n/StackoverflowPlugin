package com.victor.kochnev.plugin.stackoverflow.client;

import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;

public interface StackOverflowClient {
    AnswersResponseDto getAnswersResponse(Integer idQuestion);

    QuestionsResponseDto getQuestionsResponse(Integer idQuestion);
}
