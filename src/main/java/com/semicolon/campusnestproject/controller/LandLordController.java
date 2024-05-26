package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.UpdateLandLordApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.services.LandLordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LandLordController {

    LandLordService landLordService;

    @GetMapping("/{id}")
    public ResponseEntity<?> updateLandLordApartmentDetails(@RequestBody UpdateLandLordApartmentRequest request,
                                                            @RequestParam Long apartmentId){
        try{
            return ResponseEntity.ok(landLordService.updateLandLordApartmentDetails(apartmentId, request));
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(new ApiResponse<>(exception.getMessage()));

        }
    }

}
