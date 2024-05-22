package com.semicolon.campusnestproject.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.semicolon.campusnestproject.exception.InvalidDetailsException;

public class Validation {
    public static boolean isPhoneNumberValid(String contact) throws NumberParseException {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(contact, "IN");

        return phoneNumberUtil.isValidNumber(phoneNumber);
    }

    public static void phoneNumberValidation(String number) throws InvalidDetailsException, NumberParseException {
        if (!isPhoneNumberValid(number)){
            throw new InvalidDetailsException("invalid phone number");
        }
    }
}
