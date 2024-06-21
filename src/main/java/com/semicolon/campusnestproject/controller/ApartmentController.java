package com.semicolon.campusnestproject.controller;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.Apartment2;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.requests.DeleteApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.DeleteApartmentResponse;
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
    public ResponseEntity<List<Apartment2>> getALl() {

        List<Apartment2> apartment = apartmentService.allApartment();
        if (apartment != null) {
            return new ResponseEntity<>(apartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getLandLord/{id}")
    public ResponseEntity<?> getLandLord(@PathVariable Long id){
        try {
            Long user = apartmentService.getLandLord(id);
            return ResponseEntity.ok().body(user);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

@GetMapping("/apartments/{id}")
    public ResponseEntity<?> apartmentBy(@PathVariable Long id ){
    List<Apartment2> apartment = apartmentService.findApartmentUser(id);
    if (apartment != null) {
        return new ResponseEntity<>(apartment, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

    @GetMapping("/apartment/{id}")
    public ResponseEntity<?> apartmentById(@PathVariable Long id ){
        Apartment2 apartment = apartmentService.findApartmentById(id);
        if (apartment != null) {
            return new ResponseEntity<>(apartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
