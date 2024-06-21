package com.semicolon.campusnestproject.dtos.responses;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Response<T> {
    private String data;
    private String ref;

}
