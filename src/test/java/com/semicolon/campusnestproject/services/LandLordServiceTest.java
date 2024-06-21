package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.data.repositories.ApartmentRepository2;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import com.semicolon.campusnestproject.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
public class LandLordServiceTest {

    @Autowired
    private LandLordService landLordService;

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApartmentRepository2 apartmentRepository2;


    public RegisterLandLordRequest landlordDetails(String firstName, String lastName, String email,
                                                   String password){
        RegisterLandLordRequest registerLandlordRequest = new RegisterLandLordRequest();
        registerLandlordRequest.setFirstName(firstName);
        registerLandlordRequest.setLastName(lastName);
        registerLandlordRequest.setEmail(email);
        registerLandlordRequest.setPassword(password);

        return registerLandlordRequest;

    }

    public LoginRequest loginDetails(String email,String password){
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }



    public CompleteRegistrationRequest completeRegistrationRequest(String location,String phoneNumber, String stateOfOrigin,Long userId,String bankName, String accountNumber){
        CompleteRegistrationRequest request = new CompleteRegistrationRequest();
        request.setLocation(location);
        request.setPhoneNumber(phoneNumber);
        request.setStateOfOrigin(stateOfOrigin);
        request.setUserId(userId);
        request.setBankName(bankName);
        request.setAccountNumber(accountNumber);
        return request;
    }

    @Test void testThatALandlordCanRegister() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","joy8238545@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();

    }
    @Test void testThatALandlordCanRegister2() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatALandlordCanRegister4() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","chima11christopher@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();}
    @Test void testThatLandlordCannotRegisterWithSameEmail(){
     RegisterLandLordRequest request = landlordDetails("Landlord","Musa","joy8238545@gmail.com","PassKey@123");

        assertThrows(UserExistException.class,()->landLordService.register(request));
    }

    @Test void testThatLandlordCannotRegisterWithPasswordThatDidNotMatchTheVerification(){
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord2@gmail.com","PassK");
        assertThrows(InvalidDetailsException.class,()->landLordService.register(request));

        RegisterLandLordRequest request2 = landlordDetails("Landlord","Musa","landlord2@gmail.com","PassK123");
        assertThrows(InvalidDetailsException.class,()->landLordService.register(request2));

        RegisterLandLordRequest request3 = landlordDetails("Landlord","Musa","landlord2@gmail.com","@123");
        assertThrows(InvalidDetailsException.class,()->landLordService.register(request3));
    }

    @Test void testThatLandlordCannotRegisterWithEmailThatDidNotMatchTheVerification(){
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlordgmail.com","PassKey@");

        assertThrows(InvalidDetailsException.class,()->landLordService.register(request));

        RegisterLandLordRequest request2 = landlordDetails("Landlord","Musa","landlord@gmailcom","PassKey@");

        assertThrows(InvalidDetailsException.class,()->landLordService.register(request2));
    }


    @Test void testThatLandlordCanLogin(){
        LoginRequest request = loginDetails("joy8238545@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.login(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatLandlordCannotLoginWithWrongEmail(){
        LoginRequest request = loginDetails("land@gmail.com","PassKey@123");
      assertThrows(InvalidCredentialsException.class,()->landLordService.login(request));
    }

    @Test void testThatLandlordCannotLoginWithWrongPassword(){
        LoginRequest request = loginDetails("landlord@gmail.com","Pass@123");
        assertThrows(InvalidCredentialsException.class,()->landLordService.login(request));
    }

    @Test void testThatLandlordCannotLoginWithoutInputtingEmail(){
        LoginRequest request = loginDetails("","PassKey@123");
        assertThrows(EmptyDetailsException.class,()->landLordService.login(request));
    }

    @Test void testThatLandlordCannotLoginWithoutInputtingPassword(){
        LoginRequest request = loginDetails("landlord@gmail.com","");
        assertThrows(EmptyDetailsException.class,()->landLordService.login(request));
    }

    @Test void testThatAUserCanCompleteRegistrationAfterRegistering() throws NumberParseException, IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","Abuja",1L, "FirstBank","11111");

        File file = new File("/home/user/Pictures/flower.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.completeRegistration(request,multipartFile);

    }

    @Test void testThatAUserCanCompleteRegistrationAfterRegistering2() throws NumberParseException, IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","Abuja",2L, "Globus","121212");

        File file = new File("/home/user/Pictures/flower.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.completeRegistration(request,multipartFile);

    }

    @Test void testThatStateOfOriginFieldCannotBeEmpty() throws IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","",2L,"Oceanic","22222");
        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }

    @Test void testThatPhoneNumberFieldCannotBeEmpty() throws IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","","Ilorin",2L,"Palmpay","33333");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }

    @Test void testThatLocationFieldCannotBeEmpty() throws IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("","09062346551","Ilorin",2L,"Gtco","4444444");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }

    @Test void testThatLandlordCannotCompleteProfileWithPhoneNumberThatDidNotMatchTheVerification() throws IOException {

        CompleteRegistrationRequest request = completeRegistrationRequest("Kwara","090765488900000","Ilorin",2L,"Zenith","55555");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(InvalidDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }




    @Test void postApartment() throws IOException {
        CreatePostRequest request = new CreatePostRequest();
        request.setLandLordId(1L);
        request.setDescription("Studio2");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("MINIFLAT"));
        request.setAnnualRentFee("150000");
        request.setAgreementAndCommission("10000");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        CreatePostResponse response =landLordService.post(request,multipartFile);
        System.out.println(response.getId());
        assertThat(response).isNotNull();
    }


    @Test void postApartment2() throws IOException {
        CreatePostRequest request = new CreatePostRequest();
        request.setLandLordId(1L);
        request.setDescription("fine house");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("SELFCONTAINED"));
        request.setAnnualRentFee("6050000");
        request.setAgreementAndCommission("80000");

        File file = new File("/home/user/Pictures/house1.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        CreatePostResponse response =landLordService.post(request,multipartFile);
        System.out.println(response.getId());
        assertThat(response).isNotNull();
    }
    
    @Test void testDeleteApartment(){


        DeleteApartmentResponse2 response = landLordService.deleteApartment2(16L);
        assertThat(response).isNotNull();
        log.info("{}->",response);
    }
    @Test void testThatAUserCanRegister() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Adamu","Musa","joy@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }



  @Test
  public void findApartment(){

      System.out.println(userRepository.findById(5L));

  }

  @Test void findApartmentThatBelongsToALandlord(){

  }

//    @Test
//    public void updateLandLordApartmentDetailsTest(){
//        UpdateLandLordApartmentRequest request = new UpdateLandLordApartmentRequest();
////        request.setHouseType("MINIFLAT");
//        request.setLocation("313, Herbert Macaulay tyyxxc , Sabo-Yaba");
//
//
//        ApiResponse<UpdateLandLordResponse> response =
//                landLordService.updateLandLordApartmentDetails(2L,1L,request);
//
//        System.out.println(request);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getData().getMessage()).isNotNull();
//
//    }






}
