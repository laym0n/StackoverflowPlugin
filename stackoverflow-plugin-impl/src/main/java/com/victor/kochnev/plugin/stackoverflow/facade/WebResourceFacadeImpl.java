package com.victor.kochnev.plugin.stackoverflow.facade;

import com.victor.kochnev.integration.plugin.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.converter.StackOverflowMapper;
import com.victor.kochnev.plugin.stackoverflow.entity.StackOverflowQuestion;
import com.victor.kochnev.plugin.stackoverflow.exception.ParseDescriptionException;
import com.victor.kochnev.plugin.stackoverflow.exception.ResourceNotFoundException;
import com.victor.kochnev.plugin.stackoverflow.service.parser.ParserService;
import com.victor.kochnev.plugin.stackoverflow.service.webclient.WebClientService;
import com.victor.kochnev.plugin.stackoverflow.service.webresource.WebResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebResourceFacadeImpl implements WebResourceFacade {
    private final WebResourceService webResourceService;
    private final WebClientService webClientService;
    private final ParserService parserService;
    private final StackOverflowMapper stackOverflowMapper;

    @Override
    public CanObserveResponse canObserve(CanObserveRequest request) {
        CanObserveResponse response = new CanObserveResponse();
        response.setIsObservable(true);
        StackOverflowQuestion stackOverflowQuestion = null;
        try {
            Long questionId = parserService.parseQuestionId(request.getDescription());
            stackOverflowQuestion = webClientService.getStackOverflowInfo(questionId);
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
        StackOverflowQuestion stackOverflowQuestion = webClientService.getStackOverflowInfo(questionId);

        stackOverflowQuestion = webResourceService.create(stackOverflowQuestion);

        WebResourceDto webResourceDto = stackOverflowMapper.mapToDto(stackOverflowQuestion);
        return webResourceDto;
    }

    @Override
    public void removeFromObserve(WebResourceRemoveRequest request) {
        long questionId = Long.parseLong(request.getName());
        webResourceService.deleteByQuestionId(questionId);
    }
}
