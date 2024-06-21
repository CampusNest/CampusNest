package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Notification;
import com.semicolon.campusnestproject.dtos.requests.SendNotificationRequest;

import java.util.List;

public interface NotificationService {
    void createNotification(SendNotificationRequest sendNotificationRequest);
    List<String> listOfNotification(Long userId);
}
