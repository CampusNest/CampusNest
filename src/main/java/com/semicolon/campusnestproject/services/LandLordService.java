package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.LandLord;
import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentRequest;
import jakarta.persistence.metamodel.IdentifiableType;
import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.requests.LoginRequest;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterLandLordRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;

import java.util.Optional;

public interface LandLordService {

    Optional<LandLord> findByEmail(String email);

    void updateApartmentDescription(Long landLordId, UpdateApartmentRequest updateApartmentRequest);
    AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException;
    PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException;
    AuthenticationResponse login(LoginRequest request);
}
