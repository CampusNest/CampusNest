package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;

import java.util.List;
import java.util.Optional;

public interface ApartmentService {
    List<Apartment> findApartmentBy(String apartmentType);
    Apartment saveApartment(PostApartmentRequest request,UploadApartmentImageResponse imageRequest);
    void save(Apartment apartment);
    Apartment findById(Long apartmentId);
    void deleteApartment( Long apartmentId);
    List<Image> getApartmentImage( Long apartmentId);
    Apartment deleteImageFromApartment(Long apartmentId);
    Optional<Apartment> getApartment(Long apartmentId);
    Apartment findApartmentById(Long apartmentId);

    List<Apartment> findApartmentByUser(Long userId);

    List<Apartment> allApartment();
}
