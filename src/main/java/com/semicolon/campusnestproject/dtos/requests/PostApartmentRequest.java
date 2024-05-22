package com.semicolon.campusnestproject.dtos.requests;


import com.semicolon.campusnestproject.data.model.ApartmentType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostApartmentRequest {

    private Long landLordId;
    private String description;
    private String location;
    private ApartmentType apartmentType;
    private String annualRentFee;
    private String agreementAndCommission;
    private UploadApartmentImageRequest uploadApartmentImageRequest;

}
