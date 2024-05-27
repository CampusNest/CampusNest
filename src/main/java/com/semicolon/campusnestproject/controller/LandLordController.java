package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.DeleteApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.UpdateLandLordApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.dtos.responses.DeleteApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.services.LandLordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class LandLordController {

    LandLordService landLordService;

    @PostMapping("/postApartment")
    public ResponseEntity<?> postApartment(@RequestBody PostApartmentRequest request) {
        try {
            PostApartmentResponse response = landLordService.postApartment(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (CampusNestException | IOException e){
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/deleteApartment")
    public ResponseEntity<?> deleteApartment(@RequestBody DeleteApartmentRequest request){
        try {
            DeleteApartmentResponse response = landLordService.deleteApartment(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }catch (CampusNestException | IOException exception){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("error",exception.getMessage()));
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> updateLandLordApartmentDetails(@RequestBody UpdateLandLordApartmentRequest request,
                                                            @PathVariable Long id){
        try{
            return ResponseEntity.ok(landLordService.updateLandLordApartmentDetails(id, request));
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(new ApiResponse<>(exception.getMessage()));

        }
    }


}
