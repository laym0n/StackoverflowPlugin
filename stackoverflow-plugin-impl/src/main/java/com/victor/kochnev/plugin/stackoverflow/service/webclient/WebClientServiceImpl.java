package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;
import com.victor.kochnev.plugin.stackoverflow.client.StackOverflowClient;
import com.victor.kochnev.plugin.stackoverflow.converter.StackOverflowMapper;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientServiceImpl implements WebClientService {
    private final StackOverflowClient stackOverflowClient;
    private final StackOverflowMapper stackOverflowMapper;

    @Override
    public StackOverflowQuestion getStackOverflowInfo(Long questionId) {
        QuestionsResponseDto questionResponse = stackOverflowClient.getQuestionsResponse(questionId);
        if (questionResponse.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Question with id " + questionId + " not exists");
        }
        QuestionDto questionDto = questionResponse.getItems().get(0);
        AnswersResponseDto answersResponse = stackOverflowClient.getAnswersResponse(questionId);
        StackOverflowQuestion stackOverflowQuestion = stackOverflowMapper.mapToEntity(questionDto, answersResponse.getItems());
        return stackOverflowQuestion;
    }
}
