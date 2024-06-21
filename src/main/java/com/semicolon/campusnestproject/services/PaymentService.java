package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.dtos.responses.Response;


public interface PaymentService {
    Response<?> makePaymentForApartment(HouseRentPaymentRequest request);
    ApiResponse<?> verifyPayment(String reference);
}
