package com.victor.kochnev.plugin.stackoverflow.service.checkupdate;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;

import java.util.List;

public record CheckUpdateResult(List<StackOverflowQuestion> notUpdatedQuestions,
                                List<UpdatedResource> updatedResourceList) {
}
