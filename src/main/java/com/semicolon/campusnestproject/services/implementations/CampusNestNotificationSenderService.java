package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.dtos.requests.NotificationSenderRequest;
import com.semicolon.campusnestproject.dtos.requests.ReceiverRequest;
import com.semicolon.campusnestproject.dtos.requests.WelcomeMessageRequest;
import com.semicolon.campusnestproject.services.NotificationSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class CampusNestNotificationSenderService implements NotificationSenderService {

    private final CampusNestNotificationSetUp notificationSetUp;
    @Override
    public void welcomeMail(WelcomeMessageRequest request) {
        NotificationSenderRequest senderRequest = new NotificationSenderRequest();
        senderRequest.setName("Campus Nest");
        senderRequest.setEmail("qudusa55@gmail.com");
        List<ReceiverRequest> receivers =  new ArrayList<>();
        ReceiverRequest receiverRequest = new ReceiverRequest();
        receiverRequest.setName(request.getFirstName()+" "+request.getLastName());
        receiverRequest.setEmail(request.getEmail());
        receivers.add(receiverRequest);
        senderRequest.setTo(receivers);
        String subject = "Registration";
        String htmlContent = receiverRequest.getName()+" welcome";
        notificationSetUp.sendNotification(senderRequest,subject,htmlContent);
    }
}
