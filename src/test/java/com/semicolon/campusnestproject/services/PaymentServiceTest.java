package com.semicolon.campusnestproject.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.dtos.responses.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;





    @Test
    void testHouseRentPayment() {
        HouseRentPaymentRequest request = new HouseRentPaymentRequest();
        request.setApartmentId(2L);
        request.setUserId(3L);
        Response<?> response = paymentService.makePaymentForApartment(request);
        log.info("res-->{}", response);
        assertThat(response).isNotNull();
    }

    @Test
    void testVerification(){
        ApiResponse<?> res = paymentService.verifyPayment("iy59sikrma");
        log.info("res -> {}", res);

        String responseString = res.getData().toString();
        Pattern pattern = Pattern.compile("status=(\\w+)");
        Matcher matcher = pattern.matcher(responseString);
        if (matcher.find()) {
            String status = matcher.group(1);
            log.info("Status: {}", status);
        }
    }

}
