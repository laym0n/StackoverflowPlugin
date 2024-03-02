package com.victor.kochnev.plugin.stackoverflow.service.checkupdate;

import com.victor.kochnev.plugin.stackoverflow.BaseBootTest;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowAnswerBuilder;
import com.victor.kochnev.plugin.stackoverflow.builder.StackOverflowQuestionBuilder;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckUpdateStrategyTest extends BaseBootTest {
    @Autowired
    private CheckUpdateStrategy checkUpdateStrategy;

    @Test
    void testCheckUpdates() {
        //Assign
        List<StackOverflowQuestion> savedQuestionsList = List.of(
                StackOverflowQuestionBuilder.postfixPersistedBuilder(1L)
                        .questionId(1L)
                        .answersList(List.of(
                                StackOverflowAnswerBuilder.postfixPersistedBuilder(1L).answerId(1L).build(),
                                StackOverflowAnswerBuilder.postfixPersistedBuilder(2L).answerId(2L).build()
                        ))
                        .build(),
                StackOverflowQuestionBuilder.postfixPersistedBuilder(2L)
                        .questionId(2L)
                        .build()
        );
        List<StackOverflowQuestion> actualQuestionsList = List.of(
                StackOverflowQuestionBuilder.defaultPersistedBuilder()
                        .questionId(1L)
                        .answersList(List.of(
                                StackOverflowAnswerBuilder.postfixPersistedBuilder(1L).answerId(1L).build(),
                                StackOverflowAnswerBuilder.postfixPersistedBuilder(2L).answerId(2L).build(),
                                StackOverflowAnswerBuilder.postfixPersistedBuilder(3L).answerId(3L).build()
                        ))
                        .build(),
                StackOverflowQuestionBuilder.postfixPersistedBuilder(2L)
                        .questionId(2L)
                        .build()
        );

        //Action
        CheckUpdateResult checkUpdateResult = checkUpdateStrategy.checkUpdates(savedQuestionsList, actualQuestionsList);

        //Assert
        assertNotNull(checkUpdateResult);

        assertNotNull(checkUpdateResult.notUpdatedQuestions());
        assertEquals(1, checkUpdateResult.notUpdatedQuestions().size());
        assertEquals(2L, checkUpdateResult.notUpdatedQuestions().get(0).getQuestionId());

        assertNotNull(checkUpdateResult.updatedResourceList());
        assertEquals(1, checkUpdateResult.updatedResourceList().size());

        UpdatedResource updatedResource = checkUpdateResult.updatedResourceList().get(0);
        assertNotNull(updatedResource);
        assertEquals(StackOverflowQuestionBuilder.DEFAULT_QUESTION_ID, updatedResource.question().getQuestionId());
        assertNotNull(updatedResource.question().getAnswersList());
        assertEquals(3, updatedResource.question().getAnswersList().size());
        assertNotNull(updatedResource.newAnswerList());
        assertEquals(1, updatedResource.newAnswerList().size());
        assertEquals(3L, updatedResource.newAnswerList().get(0).getAnswerId());
    }
}
