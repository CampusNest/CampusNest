package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
class NotificationServiceTest {
    @Autowired
    private  NotificationService notificationService;

    public SendNotificationRequest sendNotificationRequest(String message, Long userID){
        SendNotificationRequest request = new SendNotificationRequest();
        request.setMessage(message);
        request.setUserId(userID);

        return request;
    }

    @Test void testThatNotificationCanSent(){
        SendNotificationRequest request = sendNotificationRequest("you just made a post",1L);
        notificationService.createNotification(request);
    }

    @Test void testThatNotificationCanSent2(){
        SendNotificationRequest request = sendNotificationRequest("you completed your profile",1L);
        notificationService.createNotification(request);
    }
 @Test void findListOfNotifications(){
     List<String> response = notificationService.listOfNotification(1L);
     log.info("-> {}",response);
     assertThat(response).isNotNull();
 }


}