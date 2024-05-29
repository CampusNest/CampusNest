package com.semicolon.campusnestproject.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.ForgotPasswordResponse;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;

public interface StudentService {
    AuthenticationResponse register(RegisterStudentRequest request) throws NumberParseException;
    AuthenticationResponse login(LoginRequest request);
    void completeRegistration(CompleteRegistrationRequest request,String email) throws NumberParseException;
    SearchApartmentResponse searchApartment(SearchApartmentRequest aptRequest) throws BudgetMustOnlyContainNumbersException;


    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);


    User findUserById(Long userId);
    User findUserForJwt(String jwt);
}
