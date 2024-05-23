package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.*;
import com.semicolon.campusnestproject.data.repositories.ImageRepository;
import com.semicolon.campusnestproject.data.repositories.LandLordRepository;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.DeleteApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.services.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CampusNestLandLordService implements LandLordService {

    private final LandLordRepository landLordRepository;
    private final ApartmentService apartmentService;
    private final CloudinaryImageUploadService cloudinaryService;
    private final UserRepository userRepository;
    private final ImageService imageService;


    @Override
    public PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException {
        PostApartmentResponse response = new PostApartmentResponse();
        Optional<User> landLord = userRepository.findById(request.getLandLordId());
        if (landLord.isEmpty()){
            throw new UserExistException("user doesn't exist");
        }

        UploadApartmentImageResponse imageRequest = cloudinaryService.uploadImage(request.getUploadApartmentImageRequest());
        Apartment apartment = apartmentService.saveApartment(request,imageRequest);
        landLord.get().getApartments().add(apartment);
        response.setId(landLord.get().getId());
        return response;
    }

    @Override
    public DeleteApartmentResponse deleteApartment(DeleteApartmentRequest deleteApartmentRequest) throws IOException {
        DeleteApartmentResponse response = new DeleteApartmentResponse();
//        Optional<User> landLord = userRepository.findById(deleteApartmentRequest.getId());
          Optional<User> landLord = userRepository.findById(deleteApartmentRequest.getId());
        if (landLord.isEmpty()){
            throw new UserExistException("user doesn't exist");
        }
        List<Apartment> apartments = landLord.get().getApartments();
        List<Image> images = apartmentService.getApartmentImage(apartments,deleteApartmentRequest.getApartmentId());
        imageService.deleteImage(images);
        cloudinaryService.deleteImage(images);
        apartmentService.deleteApartment(apartments,deleteApartmentRequest.getApartmentId());
        response.setMessage("Deleted");
        return response;
    }


}
