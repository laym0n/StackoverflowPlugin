package com.victor.kochnev.plugin.stackoverflow.entity;

import com.victor.kochnev.plugin.stackoverflow.entity.converter.StackOverflowAnswerConverter;
import com.victor.kochnev.plugin.stackoverflow.entity.converter.StackOverflowCommentConverter;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowComment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "stackoverflow_web_resource")
@SuperBuilder
@NoArgsConstructor
public class StackOverflowInfo extends BaseEntity {
    @Column(name = "question_id")
    private Integer questionId;
    @Column(name = "answers")
    @Convert(converter = StackOverflowAnswerConverter.class)
    private List<StackOverflowAnswer> answersList;
    @Column(name = "comments")
    @Convert(converter = StackOverflowCommentConverter.class)
    private List<StackOverflowComment> commentsList;
}
