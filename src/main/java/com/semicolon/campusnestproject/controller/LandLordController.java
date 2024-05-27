package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.UpdateLandLordApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.services.LandLordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LandLordController {

    LandLordService landLordService;

    @GetMapping("/{id}")
    public ResponseEntity<?> updateLandLordApartmentDetails(@RequestBody UpdateLandLordApartmentRequest request,
                                                            @RequestParam Long landLordId,
                                                            @PathVariable Long id){
        try{
            return ResponseEntity.ok(landLordService.updateLandLordApartmentDetails(id,landLordId, request));
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(new ApiResponse<>(exception.getMessage()));

        }
    }

}
