package com.semicolon.campusnestproject.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.exception.EmptyDetailsException;
import com.semicolon.campusnestproject.exception.InvalidDetailsException;

import static com.semicolon.campusnestproject.utils.Validation.phoneNumberValidation;

public class Verification {
    public static void verifyPassword(String password) throws EmptyDetailsException, InvalidDetailsException {
        if (password == null) throw new EmptyDetailsException("password is empty please provide your password");
        if(!password.matches("[A-Z][a-zA-Z]{7,}[0-9@!#$%^&():;.*_~`+{}]{1,9}")) throw new InvalidDetailsException("""
                please ensure that
                first character is a capital letter
                It contains special characters
                It contains numbers
                """);
    }
    public static void verifyEmail(String email) throws EmptyDetailsException, InvalidDetailsException {
        if (email == null) throw new EmptyDetailsException("email field is empty , please provide your email");
        if(!email.matches("[A-z0-9!#$%^&():;.*_~`+{}]+@[a-z]+[.][a-z]{2,3}")) throw new InvalidDetailsException("please ensure that email contains @ and is valid");
    }
    public static void  verifyFirstName(String firstName) throws EmptyDetailsException, InvalidDetailsException {
        if (firstName == null) throw new EmptyDetailsException("first name field is empty, please provide first name");
        if(!firstName.matches("^[A-z]+$")) throw new InvalidDetailsException("please ensure that first name contains all letters");
    }

    public static void verifyLastName(String lastName) throws EmptyDetailsException, InvalidDetailsException {
        if (lastName == null) throw new EmptyDetailsException("last name field is empty, please kindly provide lastname");
        if(!lastName.matches("^[A-z]+$")) throw new InvalidDetailsException("please ensure that last name contains all letters");
    }
    public static void verifyPhoneNumber(String phoneNumber) throws EmptyDetailsException, InvalidDetailsException, NumberParseException {
        if (phoneNumber == null) throw new EmptyDetailsException("phone number field is empty, please kindly provide phone number");
        phoneNumberValidation(phoneNumber);
    }
    public static void verifyStateOfOrigin(String stateOfOrigin) throws EmptyDetailsException, InvalidDetailsException {
        if (stateOfOrigin == null) throw new EmptyDetailsException("phone number field is empty, please kindly provide phone number");
    }

}
