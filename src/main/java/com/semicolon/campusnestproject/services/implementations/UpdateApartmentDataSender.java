package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.dtos.requests.NotificationSenderRequest;
import com.semicolon.campusnestproject.dtos.requests.ReceiverRequest;
import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentNotificationSenderRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UpdateApartmentDataSender {
    private UpdateApartmentNotificationSenderRequest sender;
    private ReceiverRequest recipient;
    private String subject;
    private String htmlContent;
}
