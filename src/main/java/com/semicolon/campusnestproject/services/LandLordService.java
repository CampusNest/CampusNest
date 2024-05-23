package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.LandLord;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import jakarta.persistence.metamodel.IdentifiableType;
import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;

import java.io.IOException;
import java.util.Optional;

public interface LandLordService {

    Optional<LandLord> findByEmail(String email);

    AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException;
    PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException;
    AuthenticationResponse login(LoginRequest request);

    ApiResponse<UpdateLandLordResponse> updateLandLordApartmentDetails(Long landLordId, Long apartmentId, UpdateLandLordApartmentRequest request);

}
