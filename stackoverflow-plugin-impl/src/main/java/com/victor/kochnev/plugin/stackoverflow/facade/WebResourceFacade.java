package com.victor.kochnev.plugin.stackoverflow.facade;

import com.victor.kochnev.platform.api.dto.*;

import java.time.Duration;

public interface WebResourceFacade {
    CanObserveResponse canObserve(CanObserveRequest request);

    WebResourceDto addForObserve(WebResourceAddRequest request);

    void removeFromObserve(WebResourceRemoveRequest request);

    void checkUpdateWebResources(Duration minimalTimeBetweenChecks);

    WebResourceDto continueForObserve(WebResourceContinueObservingRequest request);
}
