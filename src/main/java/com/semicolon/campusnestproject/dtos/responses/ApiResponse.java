package com.semicolon.campusnestproject.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class ApiResponse<T> {
    public T data;
}
