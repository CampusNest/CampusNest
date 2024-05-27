package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;



public interface PaymentService {
    ApiResponse<?> makePaymentForApartment(HouseRentPaymentRequest request);
}
