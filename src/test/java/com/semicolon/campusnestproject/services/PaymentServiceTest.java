package com.semicolon.campusnestproject.services;
import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;


    @Test
    void testHouseRentPayment(){
        HouseRentPaymentRequest request = new HouseRentPaymentRequest();
        request.setApartmentId(3L);
        request.setUserId(3L);
        ApiResponse<?> response = paymentService.makePaymentForApartment(request);
        log.info("res-->{}", response);
        assertThat(response).isNotNull();
    }
}
