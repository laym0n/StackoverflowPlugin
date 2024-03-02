package com.victor.kochnev.plugin.stackoverflow.repository;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StackOverflowQuestionRepository extends JpaRepository<StackOverflowQuestion, UUID> {
    List<StackOverflowQuestion> findByLastCheckUpdateLessThanEqual(ZonedDateTime lastCheckUpdate);

    Optional<StackOverflowQuestion> findByQuestionId(Long questionId);

    int deleteByQuestionId(Long questionId);

    @Modifying
    @Query("update StackOverflowQuestion s set s.lastCheckUpdate = ?1 where s.questionId in ?2")
    void updateLastCheckUpdateByQuestionIdIn(ZonedDateTime lastCheckUpdate, Collection<Long> questionIds);
}
