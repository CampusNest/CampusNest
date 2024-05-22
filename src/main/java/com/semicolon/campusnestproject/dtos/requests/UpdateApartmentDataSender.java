package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateApartmentDataSender {
    private UpdateNotificationSenderRequest sender;
    private ReceiverRequest recipient;
    private String subject;
    private String htmlContent;
}
