package com.semicolon.campusnestproject.dtos.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchApartmentRequest {
    private String location;
    private String apartmentType;
    private String budget;
}
