package com.victor.kochnev.plugin.stackoverflow.entity;

import com.victor.kochnev.plugin.stackoverflow.entity.converter.StackOverflowAnswerConverter;
import com.victor.kochnev.plugin.stackoverflow.entity.converter.StackOverflowCommentConverter;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowComment;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "stackoverflow_question")
@SuperBuilder
@NoArgsConstructor
public class StackOverflowQuestion extends BaseEntity {
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "answers")
    @Convert(converter = StackOverflowAnswerConverter.class)
    @Builder.Default
    private List<StackOverflowAnswer> answersList = new ArrayList<>();
    @Column(name = "comments")
    @Convert(converter = StackOverflowCommentConverter.class)
    @Builder.Default
    private List<StackOverflowComment> commentsList = new ArrayList<>();
}