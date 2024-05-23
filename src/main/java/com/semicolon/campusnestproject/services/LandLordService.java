package com.semicolon.campusnestproject.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.ForgotPasswordResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;

import java.io.IOException;

public interface LandLordService {
    AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException;
    PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException;
    AuthenticationResponse login(LoginRequest request);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);


    ApiResponse<UpdateLandLordResponse> updateLandLordApartmentDetails(Long landLordId, Long apartmentId, UpdateLandLordApartmentRequest request);
}
