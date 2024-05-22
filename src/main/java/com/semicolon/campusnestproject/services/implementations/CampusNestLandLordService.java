package com.semicolon.campusnestproject.services.implementations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.LandLord;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.LandLordRepository;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.exception.InvalidCredentialsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import com.semicolon.campusnestproject.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.semicolon.campusnestproject.utils.Verification.*;
import static com.semicolon.campusnestproject.utils.Verification.verifyPassword;

@Service
@AllArgsConstructor
public class CampusNestLandLordService implements LandLordService {

    private final LandLordRepository landLordRepository;
    private final ApartmentService apartmentService;
    private final CloudinaryImageUploadService uploadService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationSenderService notificationService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException {
        verifyLandlordDetails(request);
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .location(request.getLocation())
                .stateOfOrigin(request.getStateOfOrigin())
                .role(Role.LANDLORD)
                .build();
        userRepository.save(user);
        welcomeMessage(request);


    }

    @Override
    public PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException {
        PostApartmentResponse response = new PostApartmentResponse();
        Optional<LandLord> landLord = landLordRepository.findById(request.getLandLordId());
        if (landLord.isEmpty()){
            throw new UserExistException("user doesn't exist");
        }
        UploadApartmentImageResponse imageRequest = uploadService.uploadImage(request.getUploadApartmentImageRequest());
        Apartment apartment = apartmentService.saveApartment(request,imageRequest);
        landLord.get().getApartments().add(apartment);
        response.setId(String.valueOf(landLord.get().getId()));
        return response;
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


    }

    private void verifyLandlordDetails(RegisterLandLordRequest request) throws NumberParseException {
        if (exist(request.getEmail())) throw new UserExistException("a user with that email already exist, please provide another email");
        verifyFirstName(request.getFirstName());
        verifyLastName(request.getLastName());
        verifyPhoneNumber(request.getPhoneNumber());
        verifyEmail(request.getEmail());
        verifyStateOfOrigin(request.getStateOfOrigin());
        verifyPassword(request.getPassword());
        verifyLocation(request.getLocation());

    }

    private void welcomeMessage(RegisterLandLordRequest request) {
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
