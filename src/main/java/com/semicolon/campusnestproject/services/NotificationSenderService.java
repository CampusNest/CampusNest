package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentMessageRequest;
import com.semicolon.campusnestproject.dtos.requests.WelcomeMessageRequest;

public interface NotificationSenderService {

    void welcomeMail(WelcomeMessageRequest request);

    void updateLandLordApartmentRequestMail(UpdateApartmentMessageRequest request);
}
