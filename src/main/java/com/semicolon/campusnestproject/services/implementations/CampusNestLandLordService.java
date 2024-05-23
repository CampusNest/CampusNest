package com.semicolon.campusnestproject.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.LandLordRepository;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.semicolon.campusnestproject.utils.Verification.*;
import static java.util.Arrays.stream;

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
    private final AuthenticationService authenticationService;

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
//        welcomeMessage(request);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authenticationService.saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful");

    }


    @Override
    public PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException {
        PostApartmentResponse response = new PostApartmentResponse();
        Optional<User> landLord = userRepository.findById(request.getLandLordId());
        if (landLord.isEmpty()){
            throw new UserExistException("user doesn't exist");
        }
        UploadApartmentImageResponse imageRequest = uploadService.uploadImage(request.getUploadApartmentImageRequest());
        Apartment apartment = apartmentService.saveApartment(request, imageRequest);
        landLord.get().getApartments().add(apartment);
        response.setId(landLord.get().getId());
        return response;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        verifyLoginDetails(request);
        authenticate(request);

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with such details"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authenticationService.revokeAllTokenByUser(user);
       authenticationService.saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        verifyForgotPasswordDetails(request);
        verifyPassword(request.getPassword());
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UserNotFoundException("user not found"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setPassword(passwordEncoder.encode(user.getPassword()));

        return response;
    }

    @Override
    public DeleteApartmentResponse deleteApartment(DeleteApartmentRequest deleteApartmentRequest) throws IOException {
        DeleteApartmentResponse response = new DeleteApartmentResponse();
//        Optional<User> landLord = userRepository.findById(deleteApartmentRequest.getId());
        Optional<User> landLord = userRepository.findById(deleteApartmentRequest.getId());
        if (landLord.isEmpty()) {
            throw new UserExistException("user doesn't exist");
        }
        return null;
    }

    private void authenticate(LoginRequest request) {
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
            }}}



    @Override
    public ApiResponse<UpdateLandLordResponse> updateLandLordApartmentDetails(Long landLordId, Long apartmentId, UpdateLandLordApartmentRequest request) {
        User landLord = userRepository.findById(landLordId).orElseThrow();
        Apartment apartment = apartmentService.findById(apartmentId);

                List<JsonPatchOperation> jsonPatchOperations = new ArrayList<>();
                buildPatchOperations(request, jsonPatchOperations);
                apartment = applyPatch(jsonPatchOperations, apartment);
                apartmentService.save(apartment);
//                updateApartmentMailSender(landLord);

        return new ApiResponse<>(buildUpdateLandLordResponse());
    }

    private void updateApartmentMailSender(User landLord) {
        UpdateApartmentMessageRequest mailRequest = new UpdateApartmentMessageRequest();
        mailRequest.setLastName(landLord.getLastName());
        mailRequest.setLastName(landLord.getFirstName());
        mailRequest.setLastName(landLord.getEmail());
        notificationService.updateLandLordApartmentRequestMail(mailRequest);
    }

    private UpdateLandLordResponse buildUpdateLandLordResponse() {
        UpdateLandLordResponse response = new UpdateLandLordResponse();
        response.setMessage("Account updated successfully");
        return response;
    }

    private Apartment applyPatch(List<JsonPatchOperation> jsonPatchOperations, Apartment landLord) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonPatch jsonPatch = new JsonPatch(jsonPatchOperations);
            System.out.println(jsonPatch);
            JsonNode customerNode = mapper.convertValue(landLord, JsonNode.class);
            JsonNode updatedNode = jsonPatch.apply(customerNode);
            landLord = mapper.convertValue(updatedNode, Apartment.class);

        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
        return landLord;
    }

    private void buildPatchOperations(UpdateLandLordApartmentRequest request, List<JsonPatchOperation> jsonPatchOperations) {
        Class<?> requestClass = request.getClass();
        Field[] requestClassFields = requestClass.getDeclaredFields();
        System.out.println(Arrays.toString(requestClassFields));
        stream(requestClassFields)
                .filter(field->isValidUpdate(field, request))
                .forEach(field->addOperation(request, field, jsonPatchOperations));
    }

    private void addOperation(UpdateLandLordApartmentRequest request, Field field, List<JsonPatchOperation> jsonPatchOperations) {
        try {
            JsonPointer path = new JsonPointer("/"+ field.getName());
            JsonNode value = new TextNode(field.get(request).toString());
            ReplaceOperation replaceOperation = new ReplaceOperation(path, value);
            jsonPatchOperations.add(replaceOperation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean  isValidUpdate(Field field, UpdateLandLordApartmentRequest request) {
        field.setAccessible(true);
        try {
            return field.get(request) != null;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
