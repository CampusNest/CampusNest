package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.requests.DeleteApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.UpdateLandLordApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.ApiResponse;
import com.semicolon.campusnestproject.dtos.responses.DeleteApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.services.LandLordService;
import com.semicolon.campusnestproject.services.implementations.CampusNestLandLordService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1")

public class LandLordController {
   @Autowired
   private LandLordService landLordService;

    @PostMapping("/landlordRegister")
    public ResponseEntity<?> register(@RequestBody RegisterLandLordRequest request) {
        try {
            AuthenticationResponse response = landLordService.register(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/landlordLogin")
    public ResponseEntity<?> landlordLogin(@RequestBody LoginRequest request){
        try {
            AuthenticationResponse response = landLordService.login(request);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
                                                            @RequestParam Long landLordId,
                                                            @PathVariable Long id){
        try{
            return ResponseEntity.ok(landLordService.updateLandLordApartmentDetails(id,landLordId, request));
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(new ApiResponse<>(exception.getMessage()));

        }
    }

    @PostMapping("/landlordPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        try {
            ForgotPasswordResponse response = landLordService.forgotPassword(request);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/landlordProfile")
    public ResponseEntity<User> findUserByJwtToken(@RequestHeader("Authorization") String jwt){
        User user = landLordService.findUserForJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }



}
