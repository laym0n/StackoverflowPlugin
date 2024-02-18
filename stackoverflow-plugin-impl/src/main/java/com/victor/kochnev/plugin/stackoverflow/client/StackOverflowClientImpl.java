package com.victor.kochnev.plugin.stackoverflow.client;

import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionResponseModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {
    private WebClient webClient;

    public StackOverflowClientImpl() {
        this("https://api.stackexchange.com");
    }

    public StackOverflowClientImpl(String baseURL) {
        webClient = WebClient.create(baseURL);
    }

//    @Override
//    public AnswersResponse getAnswersResponse(Integer idQuestion) {
//        return webClient
//                .get()
//                .uri("/2.3/questions/{ids}/answers?order=desc&sort=activity&site=stackoverflow", idQuestion)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(AnswersResponse.class).block();
//    }

    @Override
    public QuestionResponseModel getQuestionsResponse(Integer idQuestion) {
        return webClient
                .get()
                .uri("/2.3/questions/{ids}?order=desc&sort=activity&site=stackoverflow", idQuestion)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(QuestionResponseModel.class).block();
    }
}
