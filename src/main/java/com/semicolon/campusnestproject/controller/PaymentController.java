package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    @GetMapping("verifyPayment/{ref}")
    public ResponseEntity<?> verifyPayment(@PathVariable String ref){
        try {
            ApiResponse<?> res = paymentService.verifyPayment(ref);
            String status = null;
            String responseString = res.getData().toString();
            Pattern pattern = Pattern.compile("status=(\\w+)");
            Matcher matcher = pattern.matcher(responseString);
            if (matcher.find()) {
                status = matcher.group(1);
            }
            return ResponseEntity.ok().body(status);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
