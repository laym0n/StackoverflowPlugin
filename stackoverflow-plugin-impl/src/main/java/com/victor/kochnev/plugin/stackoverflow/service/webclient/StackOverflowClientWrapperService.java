package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

import java.util.List;

public interface StackOverflowClientWrapperService {
    StackOverflowQuestion getStackOverflowInfo(Long questionId);

    List<StackOverflowQuestion> findAllByQuestionIds(List<Long> questionIds);
}
