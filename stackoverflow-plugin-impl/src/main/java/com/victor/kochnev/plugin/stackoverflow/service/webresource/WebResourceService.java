package com.victor.kochnev.plugin.stackoverflow.service.webresource;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

public interface WebResourceService {

    StackOverflowQuestion create(StackOverflowQuestion question);

    void deleteByQuestionId(Long questionId);
}
