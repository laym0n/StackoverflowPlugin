package com.victor.kochnev.plugin.stackoverflow.client.stackoverflow;

import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.ErrorResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;
import com.victor.kochnev.plugin.stackoverflow.config.integration.StackOverflowClientProperties;
import com.victor.kochnev.plugin.stackoverflow.exception.StackOverflowIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StackOverflowClientImpl implements StackOverflowClient {
    private final StackOverflowClientProperties clientProperties;

    @Override
    public AnswersResponseDto getAnswersResponse(Long idQuestion) {
        String uri = String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", idQuestion, clientProperties.getKey());
        log.info("Request to StackOverflow {}", uri);
        AnswersResponseDto answersResponseDto;
        try {
            answersResponseDto = WebClient.create(clientProperties.getHost())
                    .get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                    .bodyToMono(AnswersResponseDto.class).block();
        } catch (Exception e) {
            log.error("Error response from {} {}", uri, ExceptionUtils.getMessage(e));
            throw e;
        }
        log.info("Success response from {} {}", uri, answersResponseDto);
        return answersResponseDto;
    }

    @Override
    public AnswersResponseDto getAnswersResponse(List<Long> questionIds, int pageNum) {
        String uri = String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", questionIds.stream().map(Object::toString).collect(Collectors.joining(";")), clientProperties.getKey(), pageNum);
        log.info("Request to StackOverflow {}", uri);
        AnswersResponseDto answersResponseDto;
        try {
            answersResponseDto = WebClient.create(clientProperties.getHost())
                    .get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                    .bodyToMono(AnswersResponseDto.class).block();
        } catch (Exception e) {
            log.error("Error response from {} {}", uri, ExceptionUtils.getMessage(e));
            throw e;
        }
        log.info("Success response from {} {}", uri, answersResponseDto);
        return answersResponseDto;
    }

    @Override
    public QuestionsResponseDto getQuestionsResponse(Long idQuestion) {
        String uri = String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", idQuestion, clientProperties.getKey());
        log.info("Request to StackOverflow {}", uri);
        QuestionsResponseDto questionsResponseDto;
        try {
            questionsResponseDto = WebClient.create(clientProperties.getHost())
                    .get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                    .bodyToMono(QuestionsResponseDto.class).block();
        } catch (Exception e) {
            log.error("Error response from {} {}", uri, ExceptionUtils.getMessage(e));
            throw e;
        }
        log.info("Success response from {} {}", uri, questionsResponseDto);
        return questionsResponseDto;
    }

    @Override
    public QuestionsResponseDto getQuestionsResponse(List<Long> questionIds, int pageNum) {
        String uri = String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", questionIds.stream().map(Object::toString).collect(Collectors.joining(";")), clientProperties.getKey(), pageNum);
        log.info("Request to StackOverflow {}", uri);
        QuestionsResponseDto questionsResponseDto;
        try {
            questionsResponseDto = WebClient.create(clientProperties.getHost())
                    .get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                    .bodyToMono(QuestionsResponseDto.class).block();
        } catch (Exception e) {
            log.error("Error response from {} {}", uri, ExceptionUtils.getMessage(e));
            throw e;
        }
        log.info("Success response from {} {}", uri, questionsResponseDto);
        return questionsResponseDto;
    }
}
