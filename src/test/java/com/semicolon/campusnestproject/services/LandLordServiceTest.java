package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LandLordServiceTest {
    @Autowired
    LandLordService landLordService;
    @Test
    public void updateLandLordDetailsTest(){
        UpdateApartmentRequest updateApartmentRequest = new UpdateApartmentRequest();
        String email = "";
        Long landLordId = landLordService.findByEmail(email).get().getId();
        landLordService.updateApartmentDescription(landLordId, updateApartmentRequest);
    }
}
