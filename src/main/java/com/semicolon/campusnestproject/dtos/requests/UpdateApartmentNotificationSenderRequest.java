package com.semicolon.campusnestproject.dtos.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateApartmentNotificationSenderRequest {
    private String name;
    private String email;
    private ReceiverRequest recipient;
}
