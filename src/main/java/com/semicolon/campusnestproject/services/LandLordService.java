package com.semicolon.campusnestproject.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;

import java.io.IOException;

public interface LandLordService {
    AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException;
    PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException;
    AuthenticationResponse login(LoginRequest request);
    void completeRegistration(CompleteRegistrationRequest request,String email) throws NumberParseException;
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    DeleteApartmentResponse deleteApartment(DeleteApartmentRequest deleteApartmentRequest) throws IOException;
    ApiResponse<UpdateLandLordResponse> updateLandLordApartmentDetails(Long apartmentId, UpdateLandLordApartmentRequest request);
}
