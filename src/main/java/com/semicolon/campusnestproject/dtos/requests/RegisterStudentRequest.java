package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterStudentRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
