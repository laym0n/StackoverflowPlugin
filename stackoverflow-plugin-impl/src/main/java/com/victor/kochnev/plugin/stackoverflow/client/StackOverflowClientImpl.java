package com.victor.kochnev.plugin.stackoverflow.client;

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

@Service
@RequiredArgsConstructor
public class StackOverflowClientImpl implements StackOverflowClient {
    private final StackOverflowClientProperties clientProperties;

    @Override
    public AnswersResponseDto getAnswersResponse(Long idQuestion) {
        return WebClient.create(clientProperties.getHost())
                .get()
                .uri("/2.3/questions/{ids}/answers?order=desc&sort=activity&site=stackoverflow&key={key}", idQuestion, clientProperties.getKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new);
                })
                .bodyToMono(AnswersResponseDto.class).block();
    }

    @Override
    public QuestionsResponseDto getQuestionsResponse(Long idQuestion) {
        return WebClient.create(clientProperties.getHost())
                .get()
                .uri("/2.3/questions/{ids}?order=desc&sort=activity&site=stackoverflow&key={key}", idQuestion, clientProperties.getKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ErrorResponseDto.class).map(StackOverflowIntegrationException::new);
                })
                .bodyToMono(QuestionsResponseDto.class).block();
    }
}
