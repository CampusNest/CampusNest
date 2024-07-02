package com.semicolon.campusnestproject.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.*;
import com.semicolon.campusnestproject.data.repositories.ApartmentRepository2;
import com.semicolon.campusnestproject.data.repositories.ImageRepository2;
import com.semicolon.campusnestproject.data.repositories.LandLordRepository;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.UpdateLandLordResponse;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import com.semicolon.campusnestproject.exception.*;
import com.semicolon.campusnestproject.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.semicolon.campusnestproject.utils.Verification.*;
import static java.util.Arrays.stream;
import static com.semicolon.campusnestproject.utils.Verification.verifyPassword;
import static java.util.Arrays.stream;

@Service
@AllArgsConstructor
public class CampusNestLandLordService implements LandLordService {

//    private static final Logger log = LoggerFactory.getLogger(CampusNestLandLordService.class);
//    private final LandLordRepository landLordRepository;
    private final ApartmentService apartmentService;
    private final CloudinaryImageUploadService cloudinaryService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationSenderService notificationService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final ImageService imageService;
    private final CampusNestCloudinaryService nestCloudinaryService;
    private final ImageRepository2 repository2;
    private final ApartmentRepository2 apartmentRepository2;

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

    @Override
    public void completeRegistration(CompleteRegistrationRequest request, MultipartFile file) throws NumberParseException, IOException {
        verifyPhoneNumber(request.getPhoneNumber());
        verifyStateOfOrigin(request.getStateOfOrigin());
        verifyLocation(request.getLocation());
        verifyBankName(request.getBankName());
        verifyAccountNumber(request.getAccountNumber());

        User user = userRepository.findById(request.getUserId()).orElseThrow(()->new UserNotFoundException("user not found"));

        if (file == null){
            throw new EmptyDetailsException("Kindly provide an image");
        }

        String imageUrl = nestCloudinaryService.uploadImage(file).getImageUrl();
        user.setImageUrl(imageUrl);
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLocation(request.getLocation());
        user.setStateOfOrigin(request.getStateOfOrigin());
        user.setBankName(request.getBankName());
        user.setAccountNumber(request.getAccountNumber());
        userRepository.save(user);
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

    @Override
    public CreatePostResponse post(CreatePostRequest request, MultipartFile file) throws IOException {
        verifyCreatePostRequest(request);
        if (file == null){
            throw new EmptyDetailsException("Kindly provide an image");
        }

        User user = userRepository.findById(request.getLandLordId()).orElseThrow(()-> new UserNotFoundException("user does not exist"));
        Apartment2 apartment2 = new Apartment2();

        String imageUrl = nestCloudinaryService.uploadImage(file).getImageUrl();
        apartment2.setImage(imageUrl);

        apartment2.setDescription(request.getDescription());
        apartment2.setLocation(request.getLocation());
        apartment2.setAnnualRentFee(request.getAnnualRentFee());
        apartment2.setApartmentType(request.getApartmentType());
        apartment2.setAgreementAndCommission(request.getAgreementAndCommission());
        apartment2.setUser(user);
        Apartment2 apartmentValue = apartmentRepository2.save(apartment2);

        user.getApartment2s().add(apartment2);
        userRepository.save(user);


        CreatePostResponse response= new CreatePostResponse();
        response.setId(apartmentValue.getId());
        response.setMessage("Post Created successfully");

        return response;
    }

    @Override
    public DeleteApartmentResponse2 deleteApartment2(Long apartmentId) {

        Apartment2 apartment = apartmentRepository2.findById(apartmentId)
                .orElseThrow(() -> new CampusNestException("Apartment not found"));


        User user = apartment.getUser();


        user.getApartment2s().remove(apartment);

        userRepository.save(user);

        apartmentRepository2.delete(apartment);

        DeleteApartmentResponse2  response2= new DeleteApartmentResponse2();
        response2.setMessage("deleted successfully");
    return response2;

    }

    @Override
    public void addMoreImageToApartment(MultipartFile file, Long apartmentId) throws IOException {
        Apartment2 apartment2 = apartmentRepository2.findById(apartmentId).orElseThrow(()-> new CampusNestException("Apartment not found"));

        if (file == null){
            throw new CampusNestException("image not found");
        }

        String imageUrl = nestCloudinaryService.uploadImage(file).getImageUrl();
        apartment2.getGallery().add(imageUrl);

        apartmentRepository2.save(apartment2);

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
