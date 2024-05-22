package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.WelcomeMessageRequest;

public interface NotificationSenderService {

    void welcomeMail(WelcomeMessageRequest request);
}
