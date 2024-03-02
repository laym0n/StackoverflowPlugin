package com.victor.kochnev.plugin.stackoverflow.service.checkupdate;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;

import java.util.List;

public record UpdatedResource(StackOverflowQuestion question, List<StackOverflowAnswer> newAnswerList) {
}
