package com.victor.kochnev.plugin.stackoverflow.facade;

import com.victor.kochnev.integration.plugin.api.dto.CanObserveRequest;
import com.victor.kochnev.integration.plugin.api.dto.CanObserveResponse;
import com.victor.kochnev.integration.plugin.api.dto.WebResourceAddRequest;
import com.victor.kochnev.integration.plugin.api.dto.WebResourceDto;

public interface WebResourceFacade {
    CanObserveResponse canObserve(CanObserveRequest request);

    WebResourceDto addForObserve(WebResourceAddRequest request);
}
