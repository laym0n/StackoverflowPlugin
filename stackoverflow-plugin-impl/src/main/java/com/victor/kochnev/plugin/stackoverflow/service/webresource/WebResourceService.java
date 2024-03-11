package com.victor.kochnev.plugin.stackoverflow.service.webresource;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

import java.time.ZonedDateTime;
import java.util.List;

public interface WebResourceService {

    StackOverflowQuestion create(StackOverflowQuestion question);

    void deleteByQuestionId(Long questionId);

    List<StackOverflowQuestion> getResourcesWithLastCheckUpdateBefore(ZonedDateTime latestDateTimeForCheck);

    void updateAllAndSetCheckUpdateTime(List<StackOverflowQuestion> updatedQuestions, ZonedDateTime newCheckUpdateTime);

    void setCheckUpdateTimeForAll(List<StackOverflowQuestion> stackOverflowQuestions, ZonedDateTime newCheckUpdateTime);
}
