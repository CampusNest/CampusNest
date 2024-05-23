package com.semicolon.campusnestproject.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ForgotPasswordResponse {
    private String password;
}
