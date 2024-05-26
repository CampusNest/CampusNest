package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.dtos.requests.LoginRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.services.implementations.CampusNestStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentController {
    private CampusNestStudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterStudentRequest request){
      try{
          AuthenticationResponse response = studentService.register(request);
          return ResponseEntity.ok().body(response);
      }catch (Exception e){
       return ResponseEntity.badRequest().body(e.getMessage());
      }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            AuthenticationResponse response = studentService.login(request);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
