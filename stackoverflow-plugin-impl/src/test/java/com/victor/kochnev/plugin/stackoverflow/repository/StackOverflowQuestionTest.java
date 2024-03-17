package com.victor.kochnev.plugin.stackoverflow.repository;

import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowAnswerBuilder;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowQuestionBuilder;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StackOverflowQuestionTest extends BaseBootTest {
    private static UUID QUESTION_ID_1;
    private static UUID QUESTION_ID_2;
    private static UUID QUESTION_ID_3;
    private static UUID QUESTION_ID_4;

    @Test
    void testSuccessCheck() {
        //Assign
        prepareDb();
        Duration duration = Duration.of(5, ChronoUnit.MINUTES);

        //Action
        List<StackOverflowQuestion> actualResult = questionRepository.findByLastCheckUpdateLessThanEqual(ZonedDateTime.now().minus(duration));

        //Assert
        assertNotNull(actualResult);
        assertEquals(3, actualResult.size());
        assertTrue(actualResult.stream().anyMatch(question -> question.getId().equals(QUESTION_ID_2)));
        assertTrue(actualResult.stream().anyMatch(question -> question.getId().equals(QUESTION_ID_3)));
        assertTrue(actualResult.stream().anyMatch(question -> question.getId().equals(QUESTION_ID_4)));
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
}
