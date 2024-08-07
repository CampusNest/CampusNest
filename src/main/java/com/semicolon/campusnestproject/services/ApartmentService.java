package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.Apartment2;
import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.data.model.Image2;
import com.semicolon.campusnestproject.dtos.requests.DeleteApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.DeleteGalleryRequest;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.DeleteApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;

import java.util.List;
import java.util.Optional;

public interface ApartmentService {
    List<Apartment> findApartmentBy(String apartmentType);
    Apartment saveApartment(PostApartmentRequest request,UploadApartmentImageResponse imageRequest);
    void save(Apartment apartment);
    Apartment findById(Long apartmentId);
    void deleteApartment( Long apartmentId);
//    List<Image> getApartmentImage(Long apartmentId);

    String getApartmentImage(Long apartmentId);
    Apartment deleteImageFromApartment(Long apartmentId);
//    Optional<Apartment> getApartment(Long apartmentId);

    Optional<Apartment2> getApartment(Long apartmentId);
    Apartment2 findApartmentById(Long apartmentId);

    List<Apartment> findApartmentByUser(Long userId);

    List<Apartment2> findApartmentUser(Long userId);

    List<Apartment2> allApartment();

    Long getLandLord(Long apartmentId);
    List<String> getGallery(Long apartmentId);

    void deleteFileFromGallery(DeleteGalleryRequest request);


}
