package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.DeleteApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public CompleteRegistrationRequest completeRegistrationRequest(String location,String phoneNumber, String stateOfOrigin){
        CompleteRegistrationRequest request = new CompleteRegistrationRequest();
        request.setLocation(location);
        request.setPhoneNumber(phoneNumber);
        request.setStateOfOrigin(stateOfOrigin);

        return request;
    }

    @Test void testThatALandlordCanRegister() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();

    }

    @Test void testThatLandlordCannotRegisterWithSameEmail(){
     RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord@gmail.com","PassKey@123");

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
        LoginRequest request = loginDetails("landlord@gmail.com","PassKey@123");
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

    @Test void testThatAUserCanCompleteRegistrationAfterRegistering() throws NumberParseException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","Abuja");
        landLordService.completeRegistration(request,"landlord@gmail.com");

    }

    @Test void testThatAUserCannotCompleteRegistrationIfNotRegistered() throws NumberParseException {
        CompleteRegistrationRequest request = completeRegistrationRequest("ikeja","09062346551","Ilorin");
        assertThrows(UserNotFoundException.class,()-> landLordService.completeRegistration(request,"deej@gmail.com"));
    }


    @Test void testThatStateOfOriginFieldCannotBeEmpty(){
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","");

        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,"iamoluchimercy6@gmail.com"));
    }

    @Test void testThatPhoneNumberFieldCannotBeEmpty(){
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","","Ilorin");

        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,"iamoluchimercy6@gmail.com"));
    }

    @Test void testThatLocationFieldCannotBeEmpty(){
        CompleteRegistrationRequest request = completeRegistrationRequest("","09062346551","Ilorin");

        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,"iamoluchimercy6@gmail.com"));
    }

    @Test void testThatLandlordCannotCompleteProfileWithPhoneNumberThatDidNotMatchTheVerification(){

        CompleteRegistrationRequest request = completeRegistrationRequest("Kwara","090765488900000","Ilorin");

        assertThrows(InvalidDetailsException.class,()->landLordService.completeRegistration(request,"landlordgmail.com"));
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
        File file = new File("/home/user/Pictures/mmov.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        multipartFiles.add(multipartFile);
        imageRequest.setMultipartFiles(multipartFiles);
        request.setUploadApartmentImageRequest(imageRequest);
        PostApartmentResponse response = landLordService.postApartment(request);
        assertThat(response).isNotNull();
    }

    @Test
    public void deleteApartmentTest() throws IOException {
        DeleteApartmentRequest deleteApartmentRequest = new DeleteApartmentRequest();
        deleteApartmentRequest.setId(1L);
        deleteApartmentRequest.setApartmentId(4L);
        DeleteApartmentResponse response =  landLordService.deleteApartment(deleteApartmentRequest);
        assertThat(response).isNotNull();
    }
}
