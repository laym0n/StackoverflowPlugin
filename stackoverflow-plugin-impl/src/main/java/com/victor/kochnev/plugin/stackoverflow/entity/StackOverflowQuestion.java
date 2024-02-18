package com.victor.kochnev.plugin.stackoverflow.entity;

import com.victor.kochnev.plugin.stackoverflow.entity.converter.StackOverflowAnswerConverter;
import com.victor.kochnev.plugin.stackoverflow.entity.converter.StackOverflowCommentConverter;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowComment;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "stackoverflow_web_resource")
@SuperBuilder
@NoArgsConstructor
public class StackOverflowQuestion extends BaseEntity {
    @Column(name = "question_id", nullable = false)
    private Integer questionId;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "answers")
    @Convert(converter = StackOverflowAnswerConverter.class)
    private List<StackOverflowAnswer> answersList;
    @Column(name = "comments")
    @Convert(converter = StackOverflowCommentConverter.class)
    private List<StackOverflowComment> commentsList;
}
