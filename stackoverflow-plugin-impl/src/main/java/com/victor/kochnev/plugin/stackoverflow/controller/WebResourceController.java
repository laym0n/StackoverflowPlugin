package com.victor.kochnev.plugin.stackoverflow.controller;

import com.victor.kochnev.platform.api.WebResourceApi;
import com.victor.kochnev.platform.api.dto.*;
import com.victor.kochnev.plugin.stackoverflow.facade.WebResourceFacade;
import jakarta.validation.Valid;
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
    private static final String ADD_ENDPOINT = "POST /webresource";
    private static final String CAN_OBSERVE_ENDPOINT = "POST /webresource/can/observe";
    private static final String CONTINUE_OBSERVE_ENDPOINT = "PUT /webresource";
    private static final String REMOVE_ENDPOINT = "DELETE /webresource";
    private final WebResourceFacade webResourceFacade;

    @Override
    public ResponseEntity<WebResourceDto> add(WebResourceAddRequest request) {
        log.info("Get request {}", ADD_ENDPOINT);
        log.debug("Get request {} {}", ADD_ENDPOINT, request);

        WebResourceDto webResourceDto = webResourceFacade.addForObserve(request);

        log.info("Success execute {}", ADD_ENDPOINT);
        log.debug("Success execute {} {}", ADD_ENDPOINT, webResourceDto);
        return ResponseEntity.ok(webResourceDto);
    }

    @Override
    public ResponseEntity<WebResourceDto> callContinue(@Valid WebResourceContinueObservingRequest request) {
        log.info("Get request {}", CONTINUE_OBSERVE_ENDPOINT);
        log.debug("Get request {} {}", CONTINUE_OBSERVE_ENDPOINT, request);

        WebResourceDto webResourceDto = webResourceFacade.continueForObserve(request);

        log.info("Success execute {}", CONTINUE_OBSERVE_ENDPOINT);
        log.debug("Success execute {} {}", CONTINUE_OBSERVE_ENDPOINT, webResourceDto);
        return ResponseEntity.ok(webResourceDto);
    }

    @Override
    public ResponseEntity<CanObserveResponse> canObserve(CanObserveRequest request) {
        log.info("Get request {}", CAN_OBSERVE_ENDPOINT);
        log.debug("Get request {} {}", CAN_OBSERVE_ENDPOINT, request);

        CanObserveResponse response = webResourceFacade.canObserve(request);

        log.info("Success execute {}", CAN_OBSERVE_ENDPOINT);
        log.debug("Success execute {} {}", CAN_OBSERVE_ENDPOINT, response);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> remove(WebResourceRemoveRequest request) {
        log.info("Get request {}", REMOVE_ENDPOINT);
        log.debug("Get request {} {}", REMOVE_ENDPOINT, request);

        webResourceFacade.removeFromObserve(request);

        log.info("Success execute {}", REMOVE_ENDPOINT);
        return ResponseEntity.ok().build();
    }
}
