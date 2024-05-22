package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;

import java.util.List;

public interface ApartmentService {
    List<Apartment> findApartmentBy(String apartmentType);

    Apartment saveApartment(PostApartmentRequest request,UploadApartmentImageResponse imageRequest);
}
