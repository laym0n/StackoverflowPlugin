package com.victor.kochnev.plugin.stackoverflow.facade;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowAnswerBuilder;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowQuestionBuilder;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.service.webclient.StackOverflowClientWrapperService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WebResourceFacadeTest extends BaseBootTest {
    private static final String NOTIFICATION_CREATE_ENDPOINT = "/notification";
    private static UUID QUESTION_ID_1;
    private static UUID QUESTION_ID_2;
    private static UUID QUESTION_ID_3;
    private static UUID QUESTION_ID_4;
    @Autowired
    WebResourceFacade webResourceFacade;
    @MockBean
    StackOverflowClientWrapperService stackOverflowClientWrapperService;
    @Captor
    ArgumentCaptor<List<Long>> idsCaptor;

    @Test
    void testSuccessCheck() {
        //Assign
        prepareDb();
        when(stackOverflowClientWrapperService.findAllByQuestionIds(Mockito.any()))
                .thenReturn(prepareStackOverflowClientResponse());
        wireMockServer.stubFor(WireMock.post(NOTIFICATION_CREATE_ENDPOINT).willReturn(WireMock.aResponse().withStatus(200)));
        Duration duration = Duration.of(5, ChronoUnit.MINUTES);

        //Action
        webResourceFacade.checkUpdateWebResources(duration);

        //Assert
        verify(stackOverflowClientWrapperService, times(1)).findAllByQuestionIds(idsCaptor.capture());
        List<Long> idsList = idsCaptor.getValue();
        assertEquals(3, idsList.size());
        assertTrue(idsList.stream().anyMatch(i -> i.equals(2L)));
        assertTrue(idsList.stream().anyMatch(i -> i.equals(3L)));
        assertTrue(idsList.stream().anyMatch(i -> i.equals(4L)));

        StackOverflowQuestion stackOverflowQuestion1 = questionRepository.findByQuestionId(1L).get();
        assertEquals(0, stackOverflowQuestion1.getAnswersList().size());
        assertTrue(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(stackOverflowQuestion1.getLastCheckUpdate().truncatedTo(ChronoUnit.MINUTES)));

        StackOverflowQuestion stackOverflowQuestion2 = questionRepository.findByQuestionId(2L).get();
        assertEquals(1, stackOverflowQuestion2.getAnswersList().size());
        assertTrue(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(stackOverflowQuestion2.getLastCheckUpdate().truncatedTo(ChronoUnit.MINUTES)));

        StackOverflowQuestion stackOverflowQuestion3 = questionRepository.findByQuestionId(3L).get();
        assertEquals(3, stackOverflowQuestion3.getAnswersList().size());
        assertTrue(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(stackOverflowQuestion3.getLastCheckUpdate().truncatedTo(ChronoUnit.MINUTES)));

        StackOverflowQuestion stackOverflowQuestion4 = questionRepository.findByQuestionId(4L).get();
        assertEquals(1, stackOverflowQuestion4.getAnswersList().size());
        assertTrue(ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(stackOverflowQuestion4.getLastCheckUpdate().truncatedTo(ChronoUnit.MINUTES)));

        wireMockServer.verify(2, postRequestedFor(urlEqualTo(NOTIFICATION_CREATE_ENDPOINT)));
    }

    private void prepareDb() {
        QUESTION_ID_1 = questionRepository.save(StackOverflowQuestionBuilder.postfixPersistedBuilder(1L)
                .answersList(List.of())
                .lastCheckUpdate(ZonedDateTime.now()).build()).getId();
        QUESTION_ID_2 = questionRepository.save(StackOverflowQuestionBuilder.postfixPersistedBuilder(2L)
                .answersList(List.of(
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(1L).build())
                )
                .lastCheckUpdate(ZonedDateTime.now().minusDays(1)).build()).getId();
        QUESTION_ID_3 = questionRepository.save(StackOverflowQuestionBuilder.postfixPersistedBuilder(3L)
                .answersList(List.of(
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(2L).build(),
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(3L).build())
                )
                .lastCheckUpdate(ZonedDateTime.now().minusMinutes(6)).build()).getId();
        QUESTION_ID_4 = questionRepository.save(StackOverflowQuestionBuilder.postfixPersistedBuilder(4L)
                .answersList(List.of())
                .lastCheckUpdate(ZonedDateTime.now().minusMinutes(6)).build()).getId();
    }

    private List<StackOverflowQuestion> prepareStackOverflowClientResponse() {
        StackOverflowQuestion question2 = StackOverflowQuestionBuilder.postfixPersistedBuilder(2L)
                .answersList(List.of(
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(1L).build())
                )
                .lastCheckUpdate(ZonedDateTime.now().minusDays(1)).build();
        StackOverflowQuestion question3 = StackOverflowQuestionBuilder.postfixPersistedBuilder(3L)
                .answersList(List.of(
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(2L).build(),
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(3L).build(),
                        StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(4L).build())
                )
                .lastCheckUpdate(ZonedDateTime.now().minusMinutes(6)).build();
        StackOverflowQuestion question4 = StackOverflowQuestionBuilder.postfixPersistedBuilder(4L)
                .answersList(List.of(StackOverflowAnswerBuilder.defaultPersistedBuilder().answerId(5L).build()))
                .lastCheckUpdate(ZonedDateTime.now().minusMinutes(6)).build();
        return List.of(question2, question3, question4);
    }
}
