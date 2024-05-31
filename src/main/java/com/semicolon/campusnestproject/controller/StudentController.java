package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.requests.ForgotPasswordRequest;
import com.semicolon.campusnestproject.dtos.requests.LoginRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.requests.SearchApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.ForgotPasswordResponse;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.services.implementations.CampusNestStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1")

public class StudentController {
    @Autowired
    private CampusNestStudentService studentService;

    @PostMapping("/studentRegister")
    public ResponseEntity<?> register(@RequestBody RegisterStudentRequest request) {
        try {
            AuthenticationResponse response = studentService.register(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/studentLogin")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest request){
        try {
            AuthenticationResponse response = studentService.login(request);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/studentPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        try {
            ForgotPasswordResponse response = studentService.forgotPassword(request);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/searchForApartment")
    public ResponseEntity<?> searchForApartment(@RequestBody SearchApartmentRequest request){
        try{
            SearchApartmentResponse response = studentService.searchApartment(request);
            return ResponseEntity.ok(response);
        }catch (CampusNestException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

//    @GetMapping("/studentProfile")
//    public ResponseEntity<User> findUserByJwtToken(@RequestHeader("Authorization") String jwt){
//        User user = studentService.findUserForJwt(jwt);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//
//    }

    @GetMapping("/studentProfile/{id}")
    public ResponseEntity<User> findUserByJwtToken(@PathVariable Long id){
        User user = studentService.findUserBy(id);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}
