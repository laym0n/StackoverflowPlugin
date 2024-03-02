package com.victor.kochnev.plugin.stackoverflow.client.stackoverflow;

import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswersResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.ErrorResponseDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionsResponseDto;
import com.victor.kochnev.plugin.stackoverflow.config.StackOverflowClientProperties;
import com.victor.kochnev.plugin.stackoverflow.exception.StackOverflowIntegrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StackOverflowClientImpl implements StackOverflowClient {
    private final StackOverflowClientProperties clientProperties;

    @Override
    public AnswersResponseDto getAnswersResponse(Long idQuestion) {
        String uri = String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s", idQuestion, clientProperties.getKey());
        return WebClient.create(clientProperties.getHost())
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                .bodyToMono(AnswersResponseDto.class).block();
    }

    @Override
    public AnswersResponseDto getAnswersResponse(List<Long> questionIds, int pageNum) {
        String uri = String.format("/2.3/questions/%s/answers?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", questionIds.stream().map(Object::toString).collect(Collectors.joining(";")), clientProperties.getKey(), pageNum);
        return WebClient.create(clientProperties.getHost())
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                .bodyToMono(AnswersResponseDto.class).block();
    }

    @Override
    public QuestionsResponseDto getQuestionsResponse(Long idQuestion) {
        String uri = String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s", idQuestion, clientProperties.getKey());
        return WebClient.create(clientProperties.getHost())
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                .bodyToMono(QuestionsResponseDto.class).block();
    }

    @Override
    public QuestionsResponseDto getQuestionsResponse(List<Long> questionIds, int pageNum) {
        String uri = String.format("/2.3/questions/%s?order=desc&sort=activity&site=stackoverflow&key=%s&page=%s", questionIds.stream().map(Object::toString).collect(Collectors.joining(";")), clientProperties.getKey(), pageNum);
        return WebClient.create(clientProperties.getHost())
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new))
                .bodyToMono(QuestionsResponseDto.class).block();
    }
}
