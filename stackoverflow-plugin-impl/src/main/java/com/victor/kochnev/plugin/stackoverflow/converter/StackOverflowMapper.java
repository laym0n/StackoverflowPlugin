package com.victor.kochnev.plugin.stackoverflow.converter;

import com.victor.kochnev.platform.api.dto.WebResourceDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.AnswerDto;
import com.victor.kochnev.plugin.stackoverflow.api.dto.QuestionDto;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import org.mapstruct.*;

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
    @Mapping(target = "lastCheckUpdate", ignore = true)
    @Mapping(target = "description", source = "answersList", qualifiedByName = "mapToDescription")
    StackOverflowQuestion mapToEntity(QuestionDto question, List<AnswerDto> answersList);

    @BlankEntityMapping
    void update(@MappingTarget StackOverflowQuestion question, StackOverflowQuestion updated);

    List<StackOverflowAnswer> mapToValueObject(List<AnswerDto> answerList);

    @Mapping(target = "userName", source = "owner.displayName")
    @Mapping(target = "userId", source = "owner.userId")
    @Mapping(target = "createDate", source = "creationDate")
    StackOverflowAnswer mapToValueObject(AnswerDto answerList);

    @Mapping(target = "name", source = "questionId")
    @Mapping(target = "descriptionHeader", source = "title")
    WebResourceDto mapToDto(StackOverflowQuestion question);

    default ZonedDateTime mapToLocalDateTime(Long aLong) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(aLong), ZoneOffset.UTC);
    }

    @Named("mapToDescription")
    default String mapToDescription(List<AnswerDto> answersList) {
        if (answersList == null) {
            return "Количество ответов 0";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Количество ответов ").append(answersList.size()).append("\n");
        answersList
                .forEach(answer -> sb.append("Ответ от пользователя ").append(answer.getOwner().getDisplayName()).append("\n"));
        return sb.toString();
    }
}
