package com.semicolon.campusnestproject.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatePostResponse {
    private String message;
    private Long id;
}
