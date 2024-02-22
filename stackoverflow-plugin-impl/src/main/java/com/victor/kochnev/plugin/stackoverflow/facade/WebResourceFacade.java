package com.victor.kochnev.plugin.stackoverflow.facade;

import com.victor.kochnev.integration.plugin.api.dto.*;

public interface WebResourceFacade {
    CanObserveResponse canObserve(CanObserveRequest request);

    WebResourceDto addForObserve(WebResourceAddRequest request);

    void removeFromObserve(WebResourceRemoveRequest request);
}
