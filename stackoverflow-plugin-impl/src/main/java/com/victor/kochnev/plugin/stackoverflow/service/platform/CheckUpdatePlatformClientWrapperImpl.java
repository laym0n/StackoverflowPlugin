package com.victor.kochnev.plugin.stackoverflow.service.platform;

import com.victor.kochnev.platform.api.dto.NotificationCreateRequestBody;
import com.victor.kochnev.platform.api.dto.NotificationDto;
import com.victor.kochnev.platform.api.dto.WebResourceDto;
import com.victor.kochnev.platform.client.NotificationClient;
import com.victor.kochnev.plugin.stackoverflow.converter.StackOverflowMapper;
import com.victor.kochnev.plugin.stackoverflow.service.checkupdate.UpdatedResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckUpdatePlatformClientWrapperImpl implements CheckUpdatePlatformClientWrapper {
    private final NotificationClient notificationClient;
    private final StackOverflowMapper stackOverflowMapper;

    @Override
    public void sendNotification(List<UpdatedResource> questionList) {
        questionList.forEach(this::sendNotification);
    }

    private void sendNotification(UpdatedResource updatedResource) {
        var requestBody = new NotificationCreateRequestBody();

        WebResourceDto webResourceDto = stackOverflowMapper.mapToDto(updatedResource.question());
        requestBody.setUpdatedResource(webResourceDto);

        NotificationDto notificationDto = mapToDto(updatedResource);
        requestBody.setNotification(notificationDto);
        notificationClient.create(requestBody).block();
    }

    private NotificationDto mapToDto(UpdatedResource updatedResource) {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("Появилось ").append(updatedResource.newAnswerList().size()).append(" новых ответов\n");
        updatedResource.newAnswerList()
                .forEach(answer -> msgBuilder.append("Пользователь ").append(answer.getUserName()).append(" ответил на вопрос\n"));
        var notificationDto = new NotificationDto();
        notificationDto.setMessage(msgBuilder.toString());
        return notificationDto;
    }
}
