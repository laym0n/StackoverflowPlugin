package com.victor.kochnev.plugin.stackoverflow.converter;

import com.victor.kochnev.plugin.stackoverflow.api.dto.Answer;
import com.victor.kochnev.plugin.stackoverflow.api.dto.Question;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowInfo;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StackOverflowMapper {

    @Mapping(target = "questionId", source = "questionId")
    StackOverflowInfo mapToEntity(Question question);

    List<StackOverflowAnswer> mapToValueObject(List<Answer> answerList);

    @Mapping(target = "userName", source = "owner.")
    StackOverflowAnswer mapToValueObject(Answer answerList);
}
