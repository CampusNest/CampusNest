package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;

import java.io.IOException;

public interface LandLordService {


    PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException;

}