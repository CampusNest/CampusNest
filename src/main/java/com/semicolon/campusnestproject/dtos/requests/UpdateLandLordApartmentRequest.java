package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateLandLordApartmentRequest {
    private String description;
    private String location;
    private String houseType;
    private String annualRentFee;
    private String agreementAndCommission;
}
