package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/payment/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("payForRent")
    public ResponseEntity<?> payForRent(@RequestBody HouseRentPaymentRequest request){
        try{
            return ResponseEntity.ok(paymentService.makePaymentForApartment(request));
        }catch(CampusNestException exception){
            return ResponseEntity.badRequest().body(new ApiResponse<>(exception.getMessage()));
        }
    }
}
