package com.victor.kochnev.plugin.stackoverflow.converter;

import com.victor.kochnev.integration.plugin.api.dto.WebResourceDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswerDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionDto;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StackOverflowMapper {

    @BlankEntityMapping
    @Mapping(target = "questionId", source = "question.questionId")
    @Mapping(target = "answersList", source = "answersList")
    @Mapping(target = "commentsList", ignore = true)
    StackOverflowQuestion mapToEntity(QuestionDto question, List<AnswerDto> answersList);

    List<StackOverflowAnswer> mapToValueObject(List<AnswerDto> answerList);

    @Mapping(target = "userName", source = "owner.displayName")
    @Mapping(target = "userId", source = "owner.userId")
    @Mapping(target = "createDate", source = "creationDate")
    StackOverflowAnswer mapToValueObject(AnswerDto answerList);

    @Mapping(target = "name", source = "questionId")
    @Mapping(target = "description", source = "title")
    WebResourceDto mapToDto(StackOverflowQuestion question);

    default ZonedDateTime mapToLocalDateTime(Long aLong) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(aLong), ZoneOffset.UTC);
    }
}
