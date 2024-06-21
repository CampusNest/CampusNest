package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.services.NotificationSenderService;
import com.semicolon.campusnestproject.utils.HtmlContent;
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
        String htmlContent =
                HtmlContent.welcomeMessageContent(request.getFirstName()+" "+request.getLastName());
        notificationSetUp.sendNotification(senderRequest,subject,htmlContent);
    }

    @Override
    public void updateLandLordApartmentMail(UpdateApartmentMessageRequest request) {

        NotificationSenderRequest notificationRequest = new NotificationSenderRequest();
        notificationRequest.setEmail("qudusa55@gmail.com");
        notificationRequest.setName("Campus Nest");
        List<ReceiverRequest> receivers =  new ArrayList<>();
        ReceiverRequest receiverRequest = new ReceiverRequest();
        receiverRequest.setName(request.getFirstName()+" "+request.getLastName());
        receiverRequest.setEmail(request.getEmail());
        receivers.add(receiverRequest);
        notificationRequest.setTo(receivers);
        String subject = "Update Apartment Details";
        String htmlContent = receiverRequest.getName()+" apartment details updated successfully";
        notificationSetUp.sendNotification(notificationRequest,subject, htmlContent);

    }


}
