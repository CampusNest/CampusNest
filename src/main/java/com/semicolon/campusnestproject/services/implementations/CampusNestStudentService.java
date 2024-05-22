package com.semicolon.campusnestproject.services.implementations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.requests.SearchApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.JwtService;
import com.semicolon.campusnestproject.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.semicolon.campusnestproject.utils.Validation.budgetContainsNumbersOnly;
import static com.semicolon.campusnestproject.utils.Verification.*;

@Service
@AllArgsConstructor
public class CampusNestStudentService implements StudentService {

    public final ApartmentService apartmentService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterStudentRequest request) throws NumberParseException {
        verifyStudentDetails(request);
        var user = User .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .email(request.getEmail())
                .stateOfOrigin(request.getStateOfOrigin())
                .role(Role.STUDENT)
                .build();
        userRepository.save(user);
var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

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

    private void verifyStudentDetails(RegisterStudentRequest request) throws NumberParseException {
        if (exist(request.getEmail())) throw new UserExistException("a user with that email already exist, please provide another email");
        verifyFirstName(request.getFirstName());
        verifyLastName(request.getLastName());
        verifyPhoneNumber(request.getPhoneNumber());
        verifyEmail(request.getEmail());
        verifyStateOfOrigin(request.getStateOfOrigin());
        verifyPassword(request.getPassword());
        verifyLocation(request.getLocation());

    }

    private boolean exist(String email) {
        Optional<User> student = userRepository.findByEmail(email);
        return student.isPresent();
    }


}
