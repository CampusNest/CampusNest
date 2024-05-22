package com.semicolon.campusnestproject.dtos.responses;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationResponse {
    private String token;
}
