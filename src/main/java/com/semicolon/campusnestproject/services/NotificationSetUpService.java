package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.NotificationSenderRequest;

public interface NotificationSetUpService {

    void sendNotification(NotificationSenderRequest senderRequest, String subject,
                          String htmlContent);
}
