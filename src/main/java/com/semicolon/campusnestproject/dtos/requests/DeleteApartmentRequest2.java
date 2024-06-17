package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteApartmentRequest2 {
    private Long userId;
    private Long apartmentId;
}
