package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.SearchApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;

public interface StudentService {


    SearchApartmentResponse searchApartment(SearchApartmentRequest aptRequest) throws BudgetMustOnlyContainNumbersException;

}
