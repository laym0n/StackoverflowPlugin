package com.victor.kochnev.plugin.stackoverflow.service.checkupdate;

import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.entity.value.object.StackOverflowAnswer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckUpdateStrategyImpl implements CheckUpdateStrategy {

    @Override
    public CheckUpdateResult checkUpdates(List<StackOverflowQuestion> savedQuestionsList, List<StackOverflowQuestion> actualQuestionsList) {
        List<UpdatedResource> updatedResourceList = new ArrayList<>();
        List<StackOverflowQuestion> notUpdatedList = new ArrayList<>();

        // Создаем карту вопросов по их идентификаторам для быстрого доступа
        Map<Long, StackOverflowQuestion> savedQuestionsMap = mapQuestionsById(savedQuestionsList);

        // Проходимся по актуальным вопросам и проверяем наличие обновлений
        for (StackOverflowQuestion actualQuestion : actualQuestionsList) {
            StackOverflowQuestion savedQuestion = savedQuestionsMap.get(actualQuestion.getQuestionId());
            if (savedQuestion == null) {
                // Если вопрос новый, добавляем его в список результатов как целиком новый вопрос
                updatedResourceList.add(new UpdatedResource(actualQuestion, actualQuestion.getAnswersList()));
            } else {
                // Иначе сравниваем ответы на вопрос и добавляем только новые ответы в список результатов
                List<StackOverflowAnswer> newAnswers = findNewAnswers(savedQuestion, actualQuestion);
                if (!newAnswers.isEmpty()) {
                    updatedResourceList.add(new UpdatedResource(actualQuestion, newAnswers));
                } else {
                    notUpdatedList.add(savedQuestion);
                }
            }
        }

        return new CheckUpdateResult(notUpdatedList, updatedResourceList);
    }

    // Метод для создания карты вопросов по их идентификаторам
    private Map<Long, StackOverflowQuestion> mapQuestionsById(List<StackOverflowQuestion> questions) {
        Map<Long, StackOverflowQuestion> questionMap = new HashMap<>();
        for (StackOverflowQuestion question : questions) {
            questionMap.put(question.getQuestionId(), question);
        }
        return questionMap;
    }

    // Метод для поиска новых ответов на вопрос
    private List<StackOverflowAnswer> findNewAnswers(StackOverflowQuestion savedQuestion, StackOverflowQuestion actualQuestion) {
        List<StackOverflowAnswer> newAnswers = new ArrayList<>();
        Map<Long, StackOverflowAnswer> savedAnswersMap = mapAnswersById(savedQuestion.getAnswersList());

        for (StackOverflowAnswer actualAnswer : actualQuestion.getAnswersList()) {
            if (!savedAnswersMap.containsKey(actualAnswer.getAnswerId())) {
                newAnswers.add(actualAnswer);
            }
        }

        return newAnswers;
    }

    // Метод для создания карты ответов по их идентификаторам
    private Map<Long, StackOverflowAnswer> mapAnswersById(List<StackOverflowAnswer> answers) {
        Map<Long, StackOverflowAnswer> answerMap = new HashMap<>();
        for (StackOverflowAnswer answer : answers) {
            answerMap.put(answer.getAnswerId(), answer);
        }
        return answerMap;
    }
}
