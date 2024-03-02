package com.victor.kochnev.plugin.stackoverflow.service.platform;

import com.victor.kochnev.plugin.stackoverflow.client.platform.CheckUpdatePlatformClient;
import com.victor.kochnev.plugin.stackoverflow.service.checkupdate.UpdatedResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckUpdatePlatformClientWrapperImpl implements CheckUpdatePlatformClientWrapper {
//    private final CheckUpdatePlatformClient checkUpdatePlatformClient;

    @Override
    public void sendNotification(List<UpdatedResource> questionList) {

    }
}
