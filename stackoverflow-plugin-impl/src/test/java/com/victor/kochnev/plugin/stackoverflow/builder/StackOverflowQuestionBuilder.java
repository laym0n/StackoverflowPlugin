package com.victor.kochnev.plugin.stackoverflow.builder;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

import java.time.ZonedDateTime;
import java.util.UUID;

public class StackOverflowQuestionBuilder {
    public static final Long DEFAULT_QUESTION_ID = 1L;
    public static final String DEFAULT_TITLE = "Title";
    public static final String DEFAULT_DESCRIPTION = "Description";

    private StackOverflowQuestionBuilder() {
    }

    public static StackOverflowQuestion.StackOverflowQuestionBuilder<?, ?> defaultBuilder() {
        return StackOverflowQuestion.builder()
                .questionId(DEFAULT_QUESTION_ID)
                .description(DEFAULT_DESCRIPTION)
                .title(DEFAULT_TITLE);
    }

    public static StackOverflowQuestion.StackOverflowQuestionBuilder<?, ?> defaultPersistedBuilder() {
        return defaultBuilder()
                .id(UUID.randomUUID())
                .version(1L)
                .createDate(ZonedDateTime.now())
                .lastChangeDate(ZonedDateTime.now());
    }

    public static StackOverflowQuestion.StackOverflowQuestionBuilder<?, ?> postfixBuilder(Long postfix) {
        return StackOverflowQuestion.builder()
                .questionId(postfix)
                .description(DEFAULT_DESCRIPTION)
                .title(DEFAULT_TITLE);
    }

    public static StackOverflowQuestion.StackOverflowQuestionBuilder<?, ?> postfixPersistedBuilder(Long postfix) {
        return postfixBuilder(postfix)
                .id(UUID.randomUUID())
                .version(1L)
                .createDate(ZonedDateTime.now())
                .lastChangeDate(ZonedDateTime.now());
    }
}
