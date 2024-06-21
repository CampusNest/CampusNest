package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompleteStudentRegistrationRequest {
    private Long userId;
    private String stateOfOrigin;
    private String phoneNumber;
}
