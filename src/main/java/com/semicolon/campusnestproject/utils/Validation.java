package com.semicolon.campusnestproject.utils;

import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;

public class Validation {

    public static void budgetContainsNumbersOnly(String str) throws BudgetMustOnlyContainNumbersException {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new BudgetMustOnlyContainNumbersException("Budget must only contain numbers");
            }
        }
    }
}
