package com.victor.kochnev.plugin.stackoverflow.repository;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StackOverflowQuestionRepository extends JpaRepository<StackOverflowQuestion, UUID> {
    Optional<StackOverflowQuestion> findByQuestionId(Long questionId);
    int deleteByQuestionId(Long questionId);
}
