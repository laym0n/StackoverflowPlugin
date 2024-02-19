package com.victor.kochnev.plugin.stackoverflow.service.webclient;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

public interface WebClientService {
    StackOverflowQuestion getStackOverflowInfo(Long questionId);
}
