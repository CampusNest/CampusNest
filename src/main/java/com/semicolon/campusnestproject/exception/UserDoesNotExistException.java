package com.semicolon.campusnestproject.exception;

public class UserDoesNotExistException extends CampusNestException {
    public UserDoesNotExistException(String s) {
        super(s);
    }
}
