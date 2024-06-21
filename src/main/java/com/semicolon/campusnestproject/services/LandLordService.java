package com.semicolon.campusnestproject.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LandLordService {
    AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException;

    AuthenticationResponse login(LoginRequest request);
    void completeRegistration(CompleteRegistrationRequest request,MultipartFile file) throws NumberParseException, IOException;
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    ApiResponse<UpdateLandLordResponse> updateLandLordApartmentDetails(Long landlordId, Long apartmentId, UpdateLandLordApartmentRequest request);

    User findUserForJwt(String jwt);

    User findUserById(Long id);

    CreatePostResponse post(CreatePostRequest request, MultipartFile file) throws IOException;
    DeleteApartmentResponse2 deleteApartment2(Long apartmentId);
}
