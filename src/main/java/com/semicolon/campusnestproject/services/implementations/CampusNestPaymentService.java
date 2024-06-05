package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.config.PayStackConfig;
import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.requests.InitializeTransactionRequest;
import com.semicolon.campusnestproject.dtos.responses.PaystackTransactionResponse;
import com.semicolon.campusnestproject.dtos.responses.Response;
import com.semicolon.campusnestproject.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import  com.semicolon.campusnestproject.dtos.responses.ApiResponse;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class CampusNestPaymentService implements PaymentService {

    private final PayStackConfig payStackConfig;
    private final ApartmentService apartmentService;
    private final StudentService studentService;



@Override
    public Response<?> makePaymentForApartment(HouseRentPaymentRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        Apartment foundApartment = apartmentService.findApartmentById(request.getApartmentId());
        User foundUser = studentService.findUserById(request.getUserId());
        HttpEntity<InitializeTransactionRequest> transactionRequest = buildPayment(foundApartment, foundUser);
        ResponseEntity<PaystackTransactionResponse> response = restTemplate.postForEntity(payStackConfig.getPayStackBaseUrl(), transactionRequest, PaystackTransactionResponse.class);
        String url = response.getBody().getPaystackTransactionDetails().getAuthorizationUrl();
        String ref = response.getBody().getPaystackTransactionDetails().getReference();


        return new Response<>(url,ref);
    }
    @Override
    public ApiResponse<?> verifyPayment(String reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + payStackConfig.getPayStackApiKey());
        String url = "https://api.paystack.co/transaction/verify/"+ reference;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, ApiResponse.class).getBody();
    }


    private HttpEntity<InitializeTransactionRequest> buildPayment(Apartment foundApartment,User founduser) {
        InitializeTransactionRequest transactionRequest = new InitializeTransactionRequest();
        transactionRequest.setEmail(founduser.getEmail());
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(foundApartment.getAnnualRentFee())).add(
                BigDecimal.valueOf(Double.parseDouble(foundApartment.getAgreementAndCommission())));
        transactionRequest.setAmount(String.valueOf(amount.multiply(BigDecimal.valueOf(100)).
                add(BigDecimal.valueOf(7000).multiply(BigDecimal.valueOf(100)))));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+payStackConfig.getPayStackApiKey());
        return new HttpEntity<>(transactionRequest,headers);
    }



}
