package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.services.implementations.CampusNestApartmentService;
import com.semicolon.campusnestproject.services.implementations.CampusNestStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apartment")
public class ApartmentController {

    @Autowired
    private CampusNestApartmentService apartmentService;

    @GetMapping("/postedApartment/{id}")
    public ResponseEntity<List<Apartment>> getApartment(@PathVariable Long id) {

        List<Apartment> apartment = apartmentService.findApartmentByUser(id);
        if (apartment != null) {
            return new ResponseEntity<>(apartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allApartment")
    public ResponseEntity<List<Apartment>> getALl() {

        List<Apartment> apartment = apartmentService.allApartment();
        if (apartment != null) {
            return new ResponseEntity<>(apartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}