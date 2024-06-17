package com.semicolon.campusnestproject.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.dtos.requests.CreatePostRequest;
import com.semicolon.campusnestproject.dtos.requests.ForgotPasswordRequest;
import com.semicolon.campusnestproject.dtos.requests.LoginRequest;
import com.semicolon.campusnestproject.exception.EmptyDetailsException;
import com.semicolon.campusnestproject.exception.InvalidDetailsException;

import static com.semicolon.campusnestproject.utils.Validation.phoneNumberValidation;

public class Verification {
    public static void verifyPassword(String password) throws EmptyDetailsException, InvalidDetailsException {
        if (password == null|| password.trim().isEmpty()){
            throw new EmptyDetailsException("password field is empty, kindly provide your password");
        }

        if (!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()])(?!.*\\s).{6,}$")) {
            throw new InvalidDetailsException("password is too weak. It must contain letters, numbers, and special characters. The length must be greater than 5.");
        }
    }

    public static void validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new EmptyDetailsException("confirm password field is empty, kindly provide your confirm password");
        }

        if (!password.equals(confirmPassword)) {
            throw new InvalidDetailsException("confirm password does not match the password");
        }
    }
    public static void verifyEmail(String email) throws EmptyDetailsException, InvalidDetailsException {
        if (email == null || email.trim().isEmpty()) {
            throw new EmptyDetailsException("email field is empty, kindly provide your email");
        }
        if (!email.matches("[A-z0-9!#$%^&():;.*_~`+{}]+@[a-z]+[.][a-z]{2,3}")){
            throw new InvalidDetailsException("email address is not valid");
        }
    }
    public static void  verifyFirstName(String firstName) throws EmptyDetailsException, InvalidDetailsException {
        if (firstName == null || firstName.trim().isEmpty()) throw new EmptyDetailsException("first name field is empty, please provide your first name");
        if(!firstName.matches("^[A-Z][a-z]+$")) throw new InvalidDetailsException("please ensure that first name contains all letters and must start with a capital letter");
    }

    public static void verifyLastName(String lastName) throws EmptyDetailsException, InvalidDetailsException {
        if (lastName == null || lastName.trim().isEmpty()) throw new EmptyDetailsException("last name field is empty, please kindly provide your lastname");
        if(!lastName.matches("^[A-Z][a-z]+$")) throw new InvalidDetailsException("please ensure that last name contains all letters and must start with a capital letter");
    }
    public static void verifyPhoneNumber(String phoneNumber) throws EmptyDetailsException, InvalidDetailsException, NumberParseException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) throw new EmptyDetailsException("phone number field is empty, please kindly provide  your phone number");
        phoneNumberValidation(phoneNumber);
    }
    public static void verifyStateOfOrigin(String stateOfOrigin) throws EmptyDetailsException, InvalidDetailsException {
        if (stateOfOrigin == null || stateOfOrigin.trim().isEmpty()) throw new EmptyDetailsException("state Of origin field is empty, please kindly provide your state Of origin");
    }

    public static void verifyLocation(String location) throws EmptyDetailsException, InvalidDetailsException {
        if (location == null || location.trim().isEmpty()) throw new EmptyDetailsException("location field is empty, please kindly provide your location");
    }

//    public static void verifyLoginDetails(LoginRequest request){
//        if (request.getPassword() == null || request.getPassword().trim().isEmpty()){
//            throw new EmptyDetailsException("password field cannot be empty, kindly provide your password");
//        }
//
//        if (request.getEmail() == null || request.getEmail().trim().isEmpty()){
//            throw new EmptyDetailsException("email field cannot be empty, kindly provide your email");
//        }
//    }
    public static void verifyLoginDetails(LoginRequest request){
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()){
            throw new EmptyDetailsException("email field cannot be empty, kindly provide your email");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()){
            throw new EmptyDetailsException("password field cannot be empty, kindly provide your password");
        }


    }

    public static void verifyForgotPasswordDetails(ForgotPasswordRequest request){
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()){
            throw new EmptyDetailsException("password field cannot be empty, kindly provide your password");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()){
            throw new EmptyDetailsException("email field cannot be empty, kindly provide your email");
        }
    }

    public static void verifyCreatePostRequest(CreatePostRequest request){
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()){
            throw new EmptyDetailsException("description field cannot be empty, kindly provide your description");
        }


        if (request.getLocation() == null || request.getLocation().trim().isEmpty()){
            throw new EmptyDetailsException("location field cannot be empty, kindly provide your location");
        }


        if (request.getApartmentType() == null){
            throw new EmptyDetailsException("Apartment type field cannot be empty, kindly provide your apartment type");
        }

        if (request.getAnnualRentFee() == null || request.getAnnualRentFee().trim().isEmpty()){
            throw new EmptyDetailsException("annual rent fee field cannot be empty, kindly provide your annual rent fee");
        }

        if (request.getAgreementAndCommission() == null || request.getAgreementAndCommission().trim().isEmpty()){
            throw new EmptyDetailsException("agreement and commission field cannot be empty, kindly provide your agreement and commission");
        }


        if (!(request.getAnnualRentFee().matches("\\d+"))){
            throw new InvalidDetailsException("Annual rent fee must all be numbers");
        } else if (!(request.getAgreementAndCommission().matches("\\d+"))){
            throw new InvalidDetailsException("Agreement and commission fee must all be numbers");
        }



    }


}
