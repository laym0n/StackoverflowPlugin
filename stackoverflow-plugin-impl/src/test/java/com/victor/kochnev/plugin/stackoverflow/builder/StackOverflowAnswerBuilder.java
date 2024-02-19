package com.victor.kochnev.plugin.stackoverflow.builder;

import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;

import java.time.ZonedDateTime;

public class StackOverflowAnswerBuilder {
    public static final Long DEFAULT_ANSWER_ID = 1L;
    public static final Long DEFAULT_USER_ID = 1L;
    public static final String DEFAULT_USER_NAME = "User name";
    public static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.now().minusDays(10);

    private StackOverflowAnswerBuilder() {
    }

    public static StackOverflowAnswer.StackOverflowAnswerBuilder defaultPersistedBuilder() {
        return StackOverflowAnswer.builder()
                .answerId(DEFAULT_ANSWER_ID)
                .userId(DEFAULT_USER_ID)
                .createDate(DEFAULT_CREATE_DATE)
                .userName(DEFAULT_USER_NAME);
    }

    public static StackOverflowAnswer.StackOverflowAnswerBuilder postfixPersistedBuilder(long postfix) {
        return StackOverflowAnswer.builder()
                .answerId(postfix)
                .userId(postfix)
                .createDate(DEFAULT_CREATE_DATE)
                .userName(DEFAULT_USER_NAME + postfix);
    }
}
