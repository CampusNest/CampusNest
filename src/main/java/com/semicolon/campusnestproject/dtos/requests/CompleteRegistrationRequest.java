package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteRegistrationRequest {
    private Long userId;
    private String stateOfOrigin;
    private String phoneNumber;
    private String location;
    private String bankName;
    private String accountNumber;
}
