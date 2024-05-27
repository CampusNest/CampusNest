package com.semicolon.campusnestproject.dtos.requests;

import com.semicolon.campusnestproject.data.model.ApartmentType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateApartmentMessageRequest {
    private Long firstName;
    private String lastName;
    private String email;

}
