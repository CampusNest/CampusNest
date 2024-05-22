package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.LandLord;
import com.semicolon.campusnestproject.data.repositories.LandLordRepository;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.UploadApartmentImageRequest;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.exception.UserDoesNotExistException;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.CloudinaryImageUploadService;
import com.semicolon.campusnestproject.services.LandLordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CampusNextLandLordService implements LandLordService {

    private final LandLordRepository landLordRepository;
    private final ApartmentService apartmentService;
    private final CloudinaryImageUploadService uploadService;

    @Override
    public PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException {
        PostApartmentResponse response = new PostApartmentResponse();
        Optional<LandLord> landLord = landLordRepository.findById(request.getLandLordId());
        if (landLord.isEmpty()){
            throw new UserDoesNotExistException("user doesn't exist");
        }
        UploadApartmentImageResponse imageRequest = uploadService.uploadImage(request.getUploadApartmentImageRequest());
        Apartment apartment = apartmentService.saveApartment(request,imageRequest);
        landLord.get().getApartments().add(apartment);
        response.setId(String.valueOf(landLord.get().getId()));
        return response;
    }
}
