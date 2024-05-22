package com.semicolon.campusnestproject.exception;

public class BudgetMustOnlyContainNumbersException extends CampusNestException {

    public BudgetMustOnlyContainNumbersException(String message) {
        super(message);
    }
}
