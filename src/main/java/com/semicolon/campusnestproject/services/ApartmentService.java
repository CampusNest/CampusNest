package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;

import java.util.List;
import java.util.Optional;

public interface ApartmentService {
    List<Apartment> findApartmentBy(String apartmentType);

    Apartment saveApartment(PostApartmentRequest request,UploadApartmentImageResponse imageRequest);

    Apartment findById(Long apartmentId);

    void save(Apartment apartment);
}
