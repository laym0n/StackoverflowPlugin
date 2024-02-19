package com.victor.kochnev.plugin.stackoverflow.service.webresource;

import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowAnswerBuilder;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowQuestionBuilder;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WebResourceServiceTest extends BaseBootTest {
    @Autowired
    WebResourceService webResourceService;

    private static StackOverflowQuestion prepareStackOverflowQuestion() {
        return StackOverflowQuestionBuilder.defaultPersistedBuilder()
                .answersList(List.of(
                        StackOverflowAnswerBuilder.postfixPersistedBuilder(1).build(),
                        StackOverflowAnswerBuilder.postfixPersistedBuilder(2).build())
                )
                .build();
    }

    private static void assertEqualQuestions(StackOverflowQuestion expected, StackOverflowQuestion actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertTrue(expected.getCreateDate().truncatedTo(ChronoUnit.SECONDS).isEqual(
                actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS)));
        assertTrue(expected.getLastChangeDate().truncatedTo(ChronoUnit.SECONDS).isEqual(
                actual.getLastChangeDate().truncatedTo(ChronoUnit.SECONDS)));

        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getQuestionId(), actual.getQuestionId());

        var expectedAnswersList = expected.getAnswersList();
        var actualAnswersList = actual.getAnswersList();
        assertEquals(expectedAnswersList.size(), actualAnswersList.size());
        for (StackOverflowAnswer expectedAnswer : expectedAnswersList) {
            var optionalActualAnswer = actualAnswersList.stream()
                    .filter(actualAnswer -> actualAnswer.getAnswerId().equals(expectedAnswer.getAnswerId()))
                    .findFirst();
            assertTrue(optionalActualAnswer.isPresent(), () -> "Expected answer " + expectedAnswer + " not exists in actualList");
            var actualAnswer = optionalActualAnswer.get();
            assertEqualAnswers(expectedAnswer, actualAnswer);
        }
    }

    private static void assertEqualAnswers(StackOverflowAnswer expected, StackOverflowAnswer actual) {
        assertEquals(expected.getAnswerId(), actual.getAnswerId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getUserName(), actual.getUserName());
        assertTrue(expected.getCreateDate().truncatedTo(ChronoUnit.SECONDS).isEqual(actual.getCreateDate().truncatedTo(ChronoUnit.SECONDS)));
    }

    @Test
    void testCreate() {
        //Assign
        var question = prepareStackOverflowQuestion();

        //Action
        StackOverflowQuestion actualQuestion = webResourceService.create(question);

        //Assert
        assertNotNull(actualQuestion);
        assertNotNull(actualQuestion.getId());

        Optional<StackOverflowQuestion> optionalQuestion = questionRepository.findById(actualQuestion.getId());
        assertTrue(optionalQuestion.isPresent());
        StackOverflowQuestion dbQuestion = optionalQuestion.get();

        StackOverflowQuestion expectedQuestion = prepareStackOverflowQuestion();
        expectedQuestion.setId(dbQuestion.getId());
        expectedQuestion.setVersion(dbQuestion.getVersion());
        expectedQuestion.setCreateDate(dbQuestion.getCreateDate());
        expectedQuestion.setLastChangeDate(dbQuestion.getLastChangeDate());
        assertEqualQuestions(expectedQuestion, actualQuestion);
        assertEqualQuestions(expectedQuestion, dbQuestion);
    }
}
