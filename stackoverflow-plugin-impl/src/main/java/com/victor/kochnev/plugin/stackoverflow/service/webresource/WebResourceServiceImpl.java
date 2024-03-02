package com.victor.kochnev.plugin.stackoverflow.service.webresource;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import com.victor.kochnev.plugin.stackoverflow.repository.StackOverflowQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebResourceServiceImpl implements WebResourceService {
    private final StackOverflowQuestionRepository questionRepository;

    @Override
    @Transactional
    public StackOverflowQuestion create(StackOverflowQuestion question) {
        return questionRepository.save(question);
    }

    @Override
    @Transactional
    public void deleteByQuestionId(Long questionId) {
        int countDeleted = questionRepository.deleteByQuestionId(questionId);
        if (countDeleted == 0) {
            throw new ResourceNotFoundException("Web resource with questionId " + questionId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StackOverflowQuestion> getResourcesWithLastCheckUpdateBefore(ZonedDateTime latestDateTimeForCheck) {
        return questionRepository.findByLastCheckUpdateLessThanEqual(latestDateTimeForCheck);
    }

    @Override
    @Transactional
    public void updateAllAndSetChechUpdateTime(List<StackOverflowQuestion> updatedQuestions, ZonedDateTime newCheckUpdateTime) {
        updatedQuestions.forEach(question -> question.setLastCheckUpdate(newCheckUpdateTime));
        questionRepository.saveAll(updatedQuestions);
    }

    @Override
    @Transactional
    public void setCheckUpdateTimeForAll(List<StackOverflowQuestion> stackOverflowQuestions, ZonedDateTime newCheckUpdateTime) {
        List<Long> questionIdList = stackOverflowQuestions.stream().map(StackOverflowQuestion::getQuestionId).toList();
        questionRepository.updateLastCheckUpdateByQuestionIdIn(newCheckUpdateTime, questionIdList);
    }
}
