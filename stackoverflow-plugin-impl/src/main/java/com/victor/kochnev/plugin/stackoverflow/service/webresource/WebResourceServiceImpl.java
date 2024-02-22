package com.victor.kochnev.plugin.stackoverflow.service.webresource;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import com.victor.kochnev.plugin.stackoverflow.repository.StackOverflowQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
