package com.victor.kochnev.plugin.stackoverflow.controller;

import com.victor.kochnev.integration.plugin.api.WebResourceApi;
import com.victor.kochnev.integration.plugin.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.facade.WebResourceFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebResourceController implements WebResourceApi {
    private final WebResourceFacade webResourceFacade;

    @Override
    public ResponseEntity<WebResourceDto> add(@Valid WebResourceAddRequest webResourceAddRequest) {
        webResourceFacade.addForObserve(webResourceAddRequest);
        return null;
    }

    @Override
    public ResponseEntity<CanObserveResponse> canObserve(@Valid CanObserveRequest canObserveRequest) {
        CanObserveResponse response = webResourceFacade.canObserve(canObserveRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> remove(@Valid WebResourceRemoveRequest webResourceRemoveRequest) {
        return null;
    }
}
