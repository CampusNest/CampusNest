package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.requests.CompleteRegistrationRequest;
import com.semicolon.campusnestproject.dtos.requests.ForgotPasswordRequest;
import com.semicolon.campusnestproject.dtos.requests.HouseRentPaymentRequest;
import com.semicolon.campusnestproject.dtos.requests.LoginRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.ForgotPasswordResponse;
import com.semicolon.campusnestproject.exception.EmptyDetailsException;
import com.semicolon.campusnestproject.exception.InvalidCredentialsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sendinblue.ApiResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class StudentServiceTest {
    @Autowired
    private StudentService studentService;

    public RegisterStudentRequest studentDetails(String firstName, String lastName, String email,
                                                 String password){
        RegisterStudentRequest registerStudentRequest = new RegisterStudentRequest();
        registerStudentRequest.setFirstName(firstName);
        registerStudentRequest.setLastName(lastName);
        registerStudentRequest.setEmail(email);
        registerStudentRequest.setPassword(password);

        return registerStudentRequest;

    }

    public LoginRequest loginDetails(String email,String password){
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }
    public ForgotPasswordRequest forgotPasswordDetails(String email, String password){
        ForgotPasswordRequest request = new ForgotPasswordRequest();
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

    @Test
    void testThatStudentCanRegister() throws NumberParseException {
        RegisterStudentRequest request = studentDetails("divine","james","iamoluchimercy6@gmail.com","Password@123");
        AuthenticationResponse response = studentService.register(request);
        log.info("->{}",response);
        assertThat(response).isNotNull();
    }

    @Test
    void testThatStudentCanRegister2() throws NumberParseException {
        RegisterStudentRequest request = studentDetails("divine","james","Divinenuella62@gmail.com","Password@123");
        AuthenticationResponse response = studentService.register(request);
        log.info("->{}",response);
        assertThat(response).isNotNull();
    }
    @Test
    void testThatStudentCanRegister3() throws NumberParseException {
        RegisterStudentRequest request = studentDetails("divine","james","register@gmail.com","Password@123");
        AuthenticationResponse response = studentService.register(request);
        log.info("->{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatStudentCannotRegisterWithSameEmail(){
        RegisterStudentRequest request = studentDetails("divine","james","iamoluchimercy6@gmail.com","Password@123");

        assertThrows(UserExistException.class,()->studentService.register(request));
    }

    @Test void testThatFirstNameFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("","james","","Password@123");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatLastNameFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","","","Password@123");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatEmailFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","Password@123");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatPasswordFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatStudentCanLogin(){
        LoginRequest request = loginDetails("iamoluchimercy6@gmail.com","Password@123");
        AuthenticationResponse response = studentService.login(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatStudentCannotLoginWithWrongPassword(){
        LoginRequest request = loginDetails("iamoluchimercy@gmail.com","Password");
        assertThrows(InvalidCredentialsException.class, ()->studentService.login(request));
    }

    @Test void testThatStudentCannotLoginWithWrongEmail(){
        LoginRequest request = loginDetails("iamoluch@gmail.com","Password@123");
        assertThrows(InvalidCredentialsException.class, ()->studentService.login(request));
    }

    @Test void testThatStudentCannotLoginWithoutProvidingEmail(){
        LoginRequest request = loginDetails("","Password@123");
      assertThrows(EmptyDetailsException.class, ()->studentService.login(request));
    }

    @Test void testThatStudentCannotLoginWithoutProvidingPassword(){
        LoginRequest request = loginDetails("iamoluchimercy@gmail.com","");
        assertThrows(EmptyDetailsException.class, ()->studentService.login(request));
    }

    @Test void testThatAPasswordCanBeChangedIfForgotten(){
        ForgotPasswordRequest request = forgotPasswordDetails("iamoluchimercy6@gmail.com","dee@123");
        ForgotPasswordResponse  response = studentService.forgotPassword(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }
    @Test void testThatStudentCanLoginWithChangedPassword(){
        LoginRequest request = loginDetails("iamoluchimercy6@gmail.com","dee@123");
        AuthenticationResponse response = studentService.login(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatAUserCanCompleteRegistrationAfterRegistering() throws NumberParseException {
        CompleteRegistrationRequest request = completeRegistrationRequest("ikeja","09062346551","Ilorin");
        studentService.completeRegistration(request,"iamoluchimercy6@gmail.com");

    }

    @Test void testThatAUserCannotCompleteRegistrationIfNotRegistered() throws NumberParseException {
        CompleteRegistrationRequest request = completeRegistrationRequest("ikeja","09062346551","Ilorin");
        assertThrows(UserNotFoundException.class,()-> studentService.completeRegistration(request,"deej@gmail.com"));
    }


    @Test void testThatStateOfOriginFieldCannotBeEmpty(){
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","");

        assertThrows(EmptyDetailsException.class,()->studentService.completeRegistration(request,"iamoluchimercy6@gmail.com"));
    }

    @Test void testThatPhoneNumberFieldCannotBeEmpty(){
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","","Ilorin");

        assertThrows(EmptyDetailsException.class,()->studentService.completeRegistration(request,"iamoluchimercy6@gmail.com"));
    }

    @Test void testThatLocationFieldCannotBeEmpty(){
        CompleteRegistrationRequest request = completeRegistrationRequest("","09062346551","Ilorin");

        assertThrows(EmptyDetailsException.class,()->studentService.completeRegistration(request,"iamoluchimercy6@gmail.com"));
    }



}