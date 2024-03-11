package com.victor.kochnev.plugin.stackoverflow.facade;

import com.victor.kochnev.platform.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.converter.StackOverflowMapper;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.exception.ParseDescriptionException;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import com.victor.kochnev.plugin.stackoverflow.service.checkupdate.CheckUpdateResult;
import com.victor.kochnev.plugin.stackoverflow.service.checkupdate.CheckUpdateStrategy;
import com.victor.kochnev.plugin.stackoverflow.service.checkupdate.UpdatedResource;
import com.victor.kochnev.plugin.stackoverflow.service.parser.ParserService;
import com.victor.kochnev.plugin.stackoverflow.service.platform.CheckUpdatePlatformClientWrapper;
import com.victor.kochnev.plugin.stackoverflow.service.webclient.StackOverflowClientWrapperService;
import com.victor.kochnev.plugin.stackoverflow.service.webresource.WebResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebResourceFacadeImpl implements WebResourceFacade {
    private final WebResourceService webResourceService;
    private final StackOverflowClientWrapperService stackOverflowClientWrapperService;
    private final ParserService parserService;
    private final StackOverflowMapper stackOverflowMapper;
    private final CheckUpdatePlatformClientWrapper checkUpdatePlatformClientWrapper;
    private final CheckUpdateStrategy checkUpdateStrategy;

    @Override
    public CanObserveResponse canObserve(CanObserveRequest request) {
        CanObserveResponse response = new CanObserveResponse();
        response.setIsObservable(true);
        StackOverflowQuestion stackOverflowQuestion = null;
        try {
            Long questionId = parserService.parseQuestionId(request.getDescription());
            stackOverflowQuestion = stackOverflowClientWrapperService.getStackOverflowInfo(questionId);
        } catch (ParseDescriptionException | ResourceNotFoundException e) {
            log.info("Bad resource for canObserve " + ExceptionUtils.getMessage(e));
            response.setIsObservable(false);
        }
        if (Boolean.TRUE == response.getIsObservable()) {
            WebResourceDto webResourceDto = stackOverflowMapper.mapToDto(stackOverflowQuestion);
            response.setWebResource(webResourceDto);
        }
        return response;
    }

    @Override
    public WebResourceDto addForObserve(WebResourceAddRequest request) {
        Long questionId = parserService.parseQuestionId(request.getDescription());
        StackOverflowQuestion stackOverflowQuestion = stackOverflowClientWrapperService.getStackOverflowInfo(questionId);

        stackOverflowQuestion = webResourceService.create(stackOverflowQuestion);

        return stackOverflowMapper.mapToDto(stackOverflowQuestion);
    }

    @Override
    public void removeFromObserve(WebResourceRemoveRequest request) {
        long questionId = Long.parseLong(request.getName());
        webResourceService.deleteByQuestionId(questionId);
    }

    @Override
    public void checkUpdateWebResources(Duration minimalTimeBetweenChecks) {
        ZonedDateTime latestDateTimeForCheck = ZonedDateTime.now().minus(minimalTimeBetweenChecks);
        List<StackOverflowQuestion> questionListForCheck = webResourceService.getResourcesWithLastCheckUpdateBefore(latestDateTimeForCheck);
        if (questionListForCheck.isEmpty()) {
            return;
        }
        List<Long> questionIds = questionListForCheck.stream().map(StackOverflowQuestion::getQuestionId).toList();
        List<StackOverflowQuestion> actualQuestionsList = stackOverflowClientWrapperService.findAllByQuestionIds(questionIds);
        CheckUpdateResult checkUpdateResult = checkUpdateStrategy.checkUpdates(questionListForCheck, actualQuestionsList);
        List<StackOverflowQuestion> updatedQuestions = checkUpdateResult.updatedResourceList().stream().map(UpdatedResource::question).toList();
        ZonedDateTime newCheckUpdateTime = ZonedDateTime.now();
        webResourceService.updateAllAndSetCheckUpdateTime(updatedQuestions, newCheckUpdateTime);
        webResourceService.setCheckUpdateTimeForAll(checkUpdateResult.notUpdatedQuestions(), newCheckUpdateTime);
        checkUpdatePlatformClientWrapper.sendNotification(checkUpdateResult.updatedResourceList());
    }
}
