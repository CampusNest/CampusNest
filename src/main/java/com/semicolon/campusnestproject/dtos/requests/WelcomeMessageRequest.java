package com.semicolon.campusnestproject.dtos.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WelcomeMessageRequest {

    private String firstName;
    private String lastName;
    private String email;
}
