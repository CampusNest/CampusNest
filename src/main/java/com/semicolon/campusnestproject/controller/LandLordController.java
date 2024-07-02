package com.semicolon.campusnestproject.controller;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.requests.UpdateLandLordApartmentRequest;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import com.semicolon.campusnestproject.services.LandLordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

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
    public ResponseEntity<?> postApartment(
            @RequestPart(value = "image", required = false) MultipartFile multipartFile,
            @ModelAttribute CreatePostRequest request
    ) {
        try {
            CreatePostResponse response = landLordService.post(request,multipartFile);
            System.out.println("res " + response);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (CampusNestException | IOException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }




    @DeleteMapping("/deleteApartment/{apartmentId}")
    public ResponseEntity<?> deleteApartment(@PathVariable Long apartmentId){
        try {
            DeleteApartmentResponse2 response = landLordService.deleteApartment2(apartmentId);
            return ResponseEntity.ok()
                    .body(response);
        }catch (Exception exception){
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
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
            return ResponseEntity.badRequest().body(exception.getMessage());

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

    @GetMapping("/landlordProfile/{id}")
    public ResponseEntity<User> findUserByJwtToken(@PathVariable Long id){
        User user = landLordService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeRegistration( @RequestPart(value = "image", required = false) MultipartFile multipartFile,CompleteRegistrationRequest request){
        try {
            landLordService.completeRegistration(request,multipartFile);
            return ResponseEntity.ok().body("Upload successful");
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findLandlord(@PathVariable Long id){
        try{
            User user = landLordService.findUserById(id);
            return ResponseEntity.ok().body(user);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/gallery/{id}")
    public ResponseEntity<?> addToGallery(@RequestPart(value = "image" ,required = false) MultipartFile file, @PathVariable Long id){
        try {
            landLordService.addMoreImageToApartment(file,id);
            return ResponseEntity.ok().body("Added");
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }




}
