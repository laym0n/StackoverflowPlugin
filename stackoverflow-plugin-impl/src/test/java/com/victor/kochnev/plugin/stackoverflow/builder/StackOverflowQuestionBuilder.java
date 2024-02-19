package com.victor.kochnev.plugin.stackoverflow.builder;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

public class StackOverflowQuestionBuilder {
    public static final Long DEFAULT_QUESTION_ID = 1L;
    public static final String DEFAULT_TITLE = "Title";

    private StackOverflowQuestionBuilder() {
    }

    public static StackOverflowQuestion.StackOverflowQuestionBuilder<?, ?> defaultPersistedBuilder() {
        return StackOverflowQuestion.builder()
                .questionId(DEFAULT_QUESTION_ID)
                .title(DEFAULT_TITLE);
    }
}
