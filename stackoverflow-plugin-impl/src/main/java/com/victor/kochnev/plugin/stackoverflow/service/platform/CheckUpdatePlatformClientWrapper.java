package com.victor.kochnev.plugin.stackoverflow.service.platform;

import com.victor.kochnev.plugin.stackoverflow.service.checkupdate.UpdatedResource;

import java.util.List;

public interface CheckUpdatePlatformClientWrapper {

    void sendNotification(List<UpdatedResource> questionList);
}
