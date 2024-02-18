package com.victor.kochnev.plugin.stackoverflow.entity.converter;

import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;

public class StackOverflowAnswerConverter extends ListJsonConverter<StackOverflowAnswer>{
    @Override
    protected Class<StackOverflowAnswer> getInnerClass() {
        return StackOverflowAnswer.class;
    }
}
