package com.semicolon.campusnestproject.dtos.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HouseRentPaymentRequest {

    private Long apartmentId;
    private Long userId;
}
