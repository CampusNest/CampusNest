package com.semicolon.campusnestproject.services.implementations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.LoginRequest;
import com.semicolon.campusnestproject.dtos.requests.RegisterStudentRequest;
import com.semicolon.campusnestproject.dtos.requests.SearchApartmentRequest;
import com.semicolon.campusnestproject.dtos.requests.WelcomeMessageRequest;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.SearchApartmentResponse;
import com.semicolon.campusnestproject.exception.BudgetMustOnlyContainNumbersException;
import com.semicolon.campusnestproject.exception.InvalidCredentialsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.NotificationSenderService;
import com.semicolon.campusnestproject.services.StudentService;
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
        welcomeMessage(request);

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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {

            Optional<User> studentOptional = userRepository.findByEmail(request.getEmail());

            if (studentOptional.isPresent()) {
                throw new InvalidCredentialsException("Invalid password");
            } else {
                throw new InvalidCredentialsException("Invalid email");
            }
        }

        var student = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with such details"));

        var jwtToken = jwtService.generateToken(student);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void welcomeMessage(RegisterStudentRequest request) {
        WelcomeMessageRequest welcomeMessageRequest = new WelcomeMessageRequest();
        welcomeMessageRequest.setFirstName(request.getFirstName());
        welcomeMessageRequest.setLastName(request.getLastName());
        welcomeMessageRequest.setEmail(request.getEmail());
        notificationService.welcomeMail(welcomeMessageRequest);
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
