package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.exception.EmptyDetailsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class StudentServiceTest {
    @Autowired
    private StudentService studentService;

    public RegisterStudentRequest studentDetails(String firstName, String lastName, String email,
                                                 String password, String stateOfOrigin, String phoneNumber, String location){
        RegisterStudentRequest registerStudentRequest = new RegisterStudentRequest();
        registerStudentRequest.setFirstName(firstName);
        registerStudentRequest.setLastName(lastName);
        registerStudentRequest.setEmail(email);
        registerStudentRequest.setPassword(password);
        registerStudentRequest.setStateOfOrigin(stateOfOrigin);
        registerStudentRequest.setPhoneNumber(phoneNumber);
        registerStudentRequest.setLocation(location);

        return registerStudentRequest;

    }


    @Test void testThatStudentCanRegister() throws NumberParseException {
        RegisterStudentRequest request = studentDetails("divine","james","iamoluchimercy@gmail.com","Password@123","Lagos","09062346551","Lagos");
        AuthenticationResponse response = studentService.register(request);
        log.info("->{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatStudentCannotRegisterWithSameEmail(){
        RegisterStudentRequest request = studentDetails("divine","james","iamoluchimercy@gmail.com","Password@123","Lagos","09062346551","Lagos");

        assertThrows(UserExistException.class,()->studentService.register(request));
    }

    @Test void testThatFirstNameFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("","james","","Password@123","Lagos","09062346551","Lagos");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatLastNameFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","","","Password@123","Lagos","09062346551","Lagos");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatEmailFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","Password@123","Lagos","09062346551","Lagos");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatPasswordFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","","Lagos","09062346551","Lagos");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatStateOfOriginFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","Password@123","","09062346551","Lagos");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatPhoneNumberFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","Password@123","Lagos","","Lagos");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }

    @Test void testThatLocationFieldCannotBeEmpty(){
        RegisterStudentRequest request = studentDetails("divine","james","","Password@123","Lagos","09062346551","");

        assertThrows(EmptyDetailsException.class,()->studentService.register(request));
    }
}