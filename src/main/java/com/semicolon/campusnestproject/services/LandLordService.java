package com.semicolon.campusnestproject.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterLandLordRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;

import java.io.IOException;

public interface LandLordService {

    AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException;
    PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException;
}
