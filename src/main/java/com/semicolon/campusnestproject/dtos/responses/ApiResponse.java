package com.semicolon.campusnestproject.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter

public class ApiResponse<T> {
    private T data;
}
