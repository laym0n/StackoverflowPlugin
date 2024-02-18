package com.victor.kochnev.plugin.stackoverflow.entity.converter;

import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowComment;

public class StackOverflowCommentConverter extends ListJsonConverter<StackOverflowComment> {
    @Override
    protected Class<StackOverflowComment> getInnerClass() {
        return StackOverflowComment.class;
    }
}
