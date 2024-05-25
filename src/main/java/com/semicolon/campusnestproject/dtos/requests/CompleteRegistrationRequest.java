package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteRegistrationRequest {
    private String stateOfOrigin;
    private String phoneNumber;
    private String location;
}
