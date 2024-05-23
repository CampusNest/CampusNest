package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.NotificationSenderRequest;
import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentNotificationSenderRequest;

public interface NotificationSetUpService {

    void sendNotification(NotificationSenderRequest senderRequest, String subject,
                          String htmlContent);

    void updateLandLordApartmentNotification(UpdateApartmentNotificationSenderRequest senderRequest, String subject,
                                             String htmlContent);


}
