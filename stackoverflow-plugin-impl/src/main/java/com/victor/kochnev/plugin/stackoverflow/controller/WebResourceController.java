package com.victor.kochnev.plugin.stackoverflow.controller;

import com.victor.kochnev.integration.plugin.api.WebResourceApi;
import com.victor.kochnev.integration.plugin.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.facade.WebResourceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class WebResourceController implements WebResourceApi {
    private final WebResourceFacade webResourceFacade;

    @Override
    public ResponseEntity<WebResourceDto> add(WebResourceAddRequest webResourceAddRequest) {
        WebResourceDto webResourceDto = webResourceFacade.addForObserve(webResourceAddRequest);
        return ResponseEntity.ok(webResourceDto);
    }

    @Override
    public ResponseEntity<CanObserveResponse> canObserve(CanObserveRequest canObserveRequest) {
        CanObserveResponse response = webResourceFacade.canObserve(canObserveRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> remove(WebResourceRemoveRequest webResourceRemoveRequest) {
        return null;
    }
}
