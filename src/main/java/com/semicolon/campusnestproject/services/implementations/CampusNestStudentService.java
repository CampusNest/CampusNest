package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.dtos.requests.SearchApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.semicolon.campusnestproject.utils.Validation.budgetContainsNumbersOnly;

@Service
@AllArgsConstructor
public class CampusNestStudentService implements StudentService {

    public final ApartmentService apartmentService;


    @Override
    public SearchApartmentResponse searchApartment(SearchApartmentRequest aptRequest) throws BudgetMustOnlyContainNumbersException {
        budgetContainsNumbersOnly(aptRequest.getBudget());
        SearchApartmentResponse apartmentResponse = new SearchApartmentResponse();
        List<Apartment> apartments = apartmentService.findApartmentBy(aptRequest.getApartmentType().toUpperCase());
        List<Apartment> apartmentResult = apartments.stream()
                .filter(apartment -> apartment.getLocation().equalsIgnoreCase(aptRequest.getLocation())
                        && apartment.getAnnualRentFee().equals(aptRequest.getBudget()))
                .collect(Collectors.toList());
        apartmentResponse.setApartments(apartmentResult);
        return apartmentResponse;
    }





}
