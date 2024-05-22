package com.semicolon.campusnestproject.services;


import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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


    @Test void testThatStudentCanRegister(){
        RegisterStudentRequest request = studentDetails("divine","james","iamoluchimercy@gmail.com","Password@123","Lagos","09062346551","Lagos");
        AuthenticationResponse response = studentService.register(request);
        log.info("->{}",response);
        assertThat(response).isNotNull();
    }

}