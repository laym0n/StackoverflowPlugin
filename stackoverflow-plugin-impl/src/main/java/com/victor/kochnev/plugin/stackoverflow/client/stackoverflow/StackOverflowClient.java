package com.victor.kochnev.plugin.stackoverflow.client.stackoverflow;

import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;

import java.util.List;

public interface StackOverflowClient {
    AnswersResponseDto getAnswersResponse(Long idQuestion);

    AnswersResponseDto getAnswersResponse(List<Long> questionIds, int pageNum);

    QuestionsResponseDto getQuestionsResponse(Long idQuestion);

    QuestionsResponseDto getQuestionsResponse(List<Long> questionIds, int pageNum);
}
