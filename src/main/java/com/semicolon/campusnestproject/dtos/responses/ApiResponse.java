package com.semicolon.campusnestproject.dtos.responses;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor


public class ApiResponse<T> {
    public T data;

//    public Object reference;
}
