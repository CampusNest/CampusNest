package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterLandLordRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
