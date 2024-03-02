package com.victor.kochnev.plugin.stackoverflow.service.checkupdate;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

import java.util.List;

public interface CheckUpdateStrategy {
    CheckUpdateResult checkUpdates(List<StackOverflowQuestion> savedQuestionsList, List<StackOverflowQuestion> actualQuestionsList);
}
