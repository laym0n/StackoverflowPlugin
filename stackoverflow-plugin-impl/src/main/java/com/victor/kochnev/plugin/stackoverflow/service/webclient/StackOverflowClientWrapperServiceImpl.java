package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswerDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;
import com.victor.kochnev.plugin.stackoverflow.client.stackoverflow.StackOverflowClient;
import com.victor.kochnev.plugin.stackoverflow.converter.StackOverflowMapper;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StackOverflowClientWrapperServiceImpl implements StackOverflowClientWrapperService {
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
        return stackOverflowMapper.mapToEntity(questionDto, answersResponse.getItems());
    }

    @Override
    public List<StackOverflowQuestion> findAllByQuestionIds(List<Long> questionIds) {
        List<QuestionsResponseDto> questionsResponseDtoList = new ArrayList<>();
        QuestionsResponseDto questionsResponseDto;
        int pageNum = 1;
        do {
            questionsResponseDto = stackOverflowClient.getQuestionsResponse(questionIds, pageNum++);
            questionsResponseDtoList.add(questionsResponseDto);
        } while (Boolean.TRUE.equals(questionsResponseDto.getHasMore()));
        pageNum = 1;
        List<AnswersResponseDto> answersResponseDtosList = new ArrayList<>();
        AnswersResponseDto answersResponseDto;
        do {
            answersResponseDto = stackOverflowClient.getAnswersResponse(questionIds, pageNum++);
            answersResponseDtosList.add(answersResponseDto);
        } while (Boolean.TRUE.equals(answersResponseDto.getHasMore()));
        Map<Long, List<AnswerDto>> answersMap = answersResponseDtosList.stream()
                .flatMap(response -> response.getItems().stream())
                .collect(Collectors.groupingBy(AnswerDto::getQuestionId, Collectors.mapping(answerDto -> answerDto, Collectors.toList())));
        return questionsResponseDtoList.stream()
                .flatMap(questionResponseDto -> questionResponseDto.getItems().stream())
                .map(questionDto -> stackOverflowMapper.mapToEntity(questionDto, answersMap.get(questionDto.getQuestionId())))
                .toList();
    }
}
