package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteApartmentRequest {

    private Long Id;
    private Long apartmentId;
}
