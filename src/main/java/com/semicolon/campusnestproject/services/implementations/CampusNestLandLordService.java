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
import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.data.model.Role;
import com.semicolon.campusnestproject.data.model.User;
import com.semicolon.campusnestproject.data.repositories.LandLordRepository;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import com.semicolon.campusnestproject.exception.InvalidCredentialsException;
import com.semicolon.campusnestproject.exception.InvalidDetailsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import com.semicolon.campusnestproject.services.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static com.semicolon.campusnestproject.utils.Verification.verifyPassword;
import static com.semicolon.campusnestproject.utils.Verification.verifyPassword;
import static java.util.Arrays.stream;
import static java.util.Arrays.stream;

@Service
@AllArgsConstructor
public class CampusNestLandLordService implements LandLordService {

    private static final Logger log = LoggerFactory.getLogger(CampusNestLandLordService.class);
    private final LandLordRepository landLordRepository;
    private final ApartmentService apartmentService;
    private final CloudinaryImageUploadService cloudinaryService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationSenderService notificationService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final ImageService imageService;

    @Override
    public AuthenticationResponse register(RegisterLandLordRequest request) throws NumberParseException {
        verifyLandlordDetails(request);
        var user = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .email(request.getEmail().trim())
                .role(Role.LANDLORD)
                .build();
        userRepository.save(user);
          welcomeMessage(request);
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        authenticationService.saveUserToken(accessToken, refreshToken, user);

//        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setId(user.getId());
        return authenticationResponse;

    }


    @Override
    public PostApartmentResponse postApartment(PostApartmentRequest request) throws IOException {
        PostApartmentResponse response = new PostApartmentResponse();
        Optional<User> landLord = userRepository.findById(request.getLandLordId());
        if (landLord.isEmpty()){
            throw new UserExistException("user doesn't exist");
        }

        UploadApartmentImageResponse imageRequest = cloudinaryService.uploadImage(request.getUploadApartmentImageRequest());
        Apartment apartment = apartmentService.saveApartment(request,imageRequest);
        landLord.get().getApartments().add(apartment);
        userRepository.save(landLord.get());
        response.setId(landLord.get().getId());
        return response;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        verifyLoginDetails(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with such details"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password is not valid");
        }
        userRepository.save(user);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setId(user.getId());

        return response;
    }

//    @Override
//    public AuthenticationResponse login(LoginRequest request) {
//        verifyLoginDetails(request);
//        authenticate(request);
//
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("{\"error\" :\"No account found with such details\"}"));
//
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        authenticationService.revokeAllTokenByUser(user);
//       authenticationService.saveUserToken(accessToken, refreshToken, user);
//
//        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");
//    }

    @Override
    public void completeRegistration(CompleteRegistrationRequest request, String email) throws NumberParseException {
        verifyPhoneNumber(request.getPhoneNumber());
        verifyStateOfOrigin(request.getStateOfOrigin());
        verifyLocation(request.getLocation());

        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));

        user.setPhoneNumber(request.getPhoneNumber());
        user.setLocation(request.getLocation());
        user.setStateOfOrigin(request.getStateOfOrigin());
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

    @Override
    public DeleteApartmentResponse deleteApartment(DeleteApartmentRequest deleteApartmentRequest) throws IOException {
        DeleteApartmentResponse response = new DeleteApartmentResponse();
        Optional<User> landLord = userRepository.findById(deleteApartmentRequest.getLandLordId());
        if (landLord.isEmpty()) {
            throw new UserNotFoundException("User doesn't exist");
        }
        List<Image> images = apartmentService.getApartmentImage(deleteApartmentRequest.getApartmentId());
        Optional<Apartment> apartment = apartmentService.getApartment(deleteApartmentRequest.getApartmentId());
        Apartment newApartment = apartmentService.deleteImageFromApartment(apartment.get().getId());
        List<Apartment> updatedApartments = landLord.get().getApartments().stream()
                .filter(apartment2 -> apartment2.equals(newApartment))
                .toList();
        landLord.get().getApartments().clear();
        landLord.get().getApartments().addAll(updatedApartments);
        userRepository.save(landLord.get());
        imageService.deleteImage(images);
        apartmentService.deleteApartment(deleteApartmentRequest.getApartmentId());
        cloudinaryService.deleteImage(images);
        response.setMessage("Deleted");
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
                throw new InvalidCredentialsException("{\"error\" : \"Invalid password\"}");
            } else {
                throw new InvalidCredentialsException("{\"error\" : \"Invalid email\"}");
            }}}



    @Override
    public ApiResponse<UpdateLandLordResponse> updateLandLordApartmentDetails(Long landLordId, Long apartmentId, UpdateLandLordApartmentRequest request) {
        User landLord = userRepository.findById(landLordId).orElseThrow();
        Apartment apartment = apartmentService.findById(apartmentId);
        List<Apartment> apartments = landLord.getApartments();
        for (Apartment apartment1 : apartments) {
            if (apartment1.getId().equals(apartment.getId())) {
                List<JsonPatchOperation> jsonPatchOperations = new ArrayList<>();
                buildPatchOperations(request, jsonPatchOperations);
                apartment = applyPatch(jsonPatchOperations, apartment);
                apartmentService.save(apartment);
                break;
            }

        }
        updateApartmentMailSender(landLord);
        return new ApiResponse<>(buildUpdateLandLordResponse());
    }

    @Override
    public User findUserForJwt(String jwt) {
        String email = jwtService.getEmailFromJwtToken(jwt);

        return userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("email is does not exist"));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException("user does not exist"));
    }

    private void updateApartmentMailSender(User landLord) {
        UpdateApartmentMessageRequest mailRequest = new UpdateApartmentMessageRequest();
        mailRequest.setLastName(landLord.getLastName());
        mailRequest.setLastName(landLord.getFirstName());
        mailRequest.setLastName(landLord.getEmail());
        notificationService.updateLandLordApartmentMail(mailRequest);
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
        verifyEmail(request.getEmail());
        verifyPassword(request.getPassword());

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
