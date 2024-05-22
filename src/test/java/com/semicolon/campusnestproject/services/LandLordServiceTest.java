package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterLandLordRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.requests.UploadApartmentImageRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.exception.UserExistException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
public class LandLordServiceTest {

    @Autowired
    private LandLordService landLordService;


    public RegisterLandLordRequest landlordDetails(String firstName, String lastName, String email,
                                                   String password, String stateOfOrigin, String phoneNumber, String location){
        RegisterLandLordRequest registerLandlordRequest = new RegisterLandLordRequest();
        registerLandlordRequest.setFirstName(firstName);
        registerLandlordRequest.setLastName(lastName);
        registerLandlordRequest.setEmail(email);
        registerLandlordRequest.setPassword(password);
        registerLandlordRequest.setStateOfOrigin(stateOfOrigin);
        registerLandlordRequest.setPhoneNumber(phoneNumber);
        registerLandlordRequest.setLocation(location);

        return registerLandlordRequest;

    }

    @Test void testThatALandlordCanRegister() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord@gmail.com","PassKey@123","Ogun","09034567893","Benin");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();

    }

    @Test void testThatLandlordCannotRegisterWithSameEmail(){
     RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord@gmail.com","PassKey@123","Ogun","09034567893","Benin");

        assertThrows(UserExistException.class,()->landLordService.register(request));
    }


    @Test
    public void postApartmentTest() throws IOException {

        List<MultipartFile> multipartFiles = new ArrayList<>();
        PostApartmentRequest request = new PostApartmentRequest();
        request.setLandLordId(1L);
        request.setDescription("Fine House");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("MINIFLAT"));
        request.setAnnualRentFee("150000");
        request.setAgreementAndCommission("10000");
        UploadApartmentImageRequest imageRequest = new UploadApartmentImageRequest();
        byte[] imageBytes = "example image content".getBytes();
        MultipartFile multipartFile = new MockMultipartFile(
                "example.jpg",
                "example.jpg",
                "image/jpeg",
                imageBytes);
        multipartFiles.add(multipartFile);
        imageRequest.setMultipartFiles(multipartFiles);
        request.setUploadApartmentImageRequest(imageRequest);
        PostApartmentResponse response = landLordService.postApartment(request);
        assertThat(response).isNotNull();
    }
}
