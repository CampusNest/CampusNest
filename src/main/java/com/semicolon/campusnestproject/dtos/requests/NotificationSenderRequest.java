package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class NotificationSenderRequest {
    private String name;
    private String email;
    private List<ReceiverRequest> to;
}
