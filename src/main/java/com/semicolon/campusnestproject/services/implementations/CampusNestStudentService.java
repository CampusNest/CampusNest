package com.semicolon.campusnestproject.services.implementations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.dtos.requests.SearchApartmentRequest;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.ForgotPasswordResponse;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.StudentService;
import com.semicolon.campusnestproject.exception.InvalidCredentialsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import com.semicolon.campusnestproject.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final NotificationSenderService notificationService;
    private final AuthenticationService authenticationService;

    @Override
    public AuthenticationResponse register(RegisterStudentRequest request) throws NumberParseException {
        verifyStudentDetails(request);
        var user = User .builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .email(request.getEmail().trim())
                .role(Role.STUDENT)
                .build();
        userRepository.save(user);
//        welcomeMessage(request);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authenticationService.saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful");


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

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        verifyLoginDetails(request);
        authenticate(request);

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with such details"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authenticationService.saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful");

    }

    @Override
    public void completeRegistration(CompleteRegistrationRequest request, String email) throws NumberParseException {
        verifyPhoneNumber(request.getPhoneNumber());
        verifyStateOfOrigin(request.getStateOfOrigin());
        verifyLocation(request.getLocation());

        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));

         user.setPhoneNumber(request.getPhoneNumber().trim());
         user.setLocation(request.getLocation().trim());
         user.setStateOfOrigin(request.getStateOfOrigin().trim());
         userRepository.save(user);
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        verifyForgotPasswordDetails(request);
        verifyPassword(request.getPassword());
        User user = userRepository.findByEmail(request.getEmail().trim()).orElseThrow(()-> new UserNotFoundException("user not found"));

        user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        userRepository.save(user);

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setPassword(passwordEncoder.encode(user.getPassword()));

        return response;
    }

    private void authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail().trim(), request.getPassword().trim())
            );
        } catch (BadCredentialsException ex) {

            Optional<User> studentOptional = userRepository.findByEmail(request.getEmail());

            if (studentOptional.isPresent()) {
                throw new InvalidCredentialsException("Invalid password");
            } else {
                throw new InvalidCredentialsException("Invalid email");
            }
        }
    }


    private void verifyStudentDetails(RegisterStudentRequest request) throws NumberParseException {
        if (exist(request.getEmail())) throw new UserExistException("a user with that email already exist, please provide another email");
        verifyFirstName(request.getFirstName());
        verifyLastName(request.getLastName());
        verifyEmail(request.getEmail());
        verifyPassword(request.getPassword());

    }

    private void welcomeMessage(RegisterStudentRequest request) {
        WelcomeMessageRequest welcomeMessageRequest = new WelcomeMessageRequest();
        welcomeMessageRequest.setFirstName(request.getFirstName());
        welcomeMessageRequest.setLastName(request.getLastName());
        welcomeMessageRequest.setEmail(request.getEmail());
        notificationService.welcomeMail(welcomeMessageRequest);
    }

    private boolean exist(String email) {
        Optional<User> student = userRepository.findByEmail(email);
        return student.isPresent();
    }




}
