package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.data.repositories.ApartmentRepository2;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.*;
import com.semicolon.campusnestproject.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
public class LandLordServiceTest {

    @Autowired
    private LandLordService landLordService;

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApartmentRepository2 apartmentRepository2;


    public RegisterLandLordRequest landlordDetails(String firstName, String lastName, String email,
                                                   String password){
        RegisterLandLordRequest registerLandlordRequest = new RegisterLandLordRequest();
        registerLandlordRequest.setFirstName(firstName);
        registerLandlordRequest.setLastName(lastName);
        registerLandlordRequest.setEmail(email);
        registerLandlordRequest.setPassword(password);

        return registerLandlordRequest;

    }

    public LoginRequest loginDetails(String email,String password){
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }



    public CompleteRegistrationRequest completeRegistrationRequest(String location,String phoneNumber, String stateOfOrigin,Long userId,String bankName, String accountNumber){
        CompleteRegistrationRequest request = new CompleteRegistrationRequest();
        request.setLocation(location);
        request.setPhoneNumber(phoneNumber);
        request.setStateOfOrigin(stateOfOrigin);
        request.setUserId(userId);
        request.setBankName(bankName);
        request.setAccountNumber(accountNumber);
        return request;
    }

    @Test void testThatALandlordCanRegister() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","joy8238545@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();

    }
    @Test void testThatALandlordCanRegister2() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatALandlordCanRegister4() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","chima11christopher@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();}
    @Test void testThatLandlordCannotRegisterWithSameEmail(){
     RegisterLandLordRequest request = landlordDetails("Landlord","Musa","joy8238545@gmail.com","PassKey@123");

        assertThrows(UserExistException.class,()->landLordService.register(request));
    }

    @Test void testThatLandlordCannotRegisterWithPasswordThatDidNotMatchTheVerification(){
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlord2@gmail.com","PassK");
        assertThrows(InvalidDetailsException.class,()->landLordService.register(request));

        RegisterLandLordRequest request2 = landlordDetails("Landlord","Musa","landlord2@gmail.com","PassK123");
        assertThrows(InvalidDetailsException.class,()->landLordService.register(request2));

        RegisterLandLordRequest request3 = landlordDetails("Landlord","Musa","landlord2@gmail.com","@123");
        assertThrows(InvalidDetailsException.class,()->landLordService.register(request3));
    }

    @Test void testThatLandlordCannotRegisterWithEmailThatDidNotMatchTheVerification(){
        RegisterLandLordRequest request = landlordDetails("Landlord","Musa","landlordgmail.com","PassKey@");

        assertThrows(InvalidDetailsException.class,()->landLordService.register(request));

        RegisterLandLordRequest request2 = landlordDetails("Landlord","Musa","landlord@gmailcom","PassKey@");

        assertThrows(InvalidDetailsException.class,()->landLordService.register(request2));
    }


    @Test void testThatLandlordCanLogin(){
        LoginRequest request = loginDetails("joy8238545@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.login(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }

    @Test void testThatLandlordCannotLoginWithWrongEmail(){
        LoginRequest request = loginDetails("land@gmail.com","PassKey@123");
      assertThrows(InvalidCredentialsException.class,()->landLordService.login(request));
    }

    @Test void testThatLandlordCannotLoginWithWrongPassword(){
        LoginRequest request = loginDetails("landlord@gmail.com","Pass@123");
        assertThrows(InvalidCredentialsException.class,()->landLordService.login(request));
    }

    @Test void testThatLandlordCannotLoginWithoutInputtingEmail(){
        LoginRequest request = loginDetails("","PassKey@123");
        assertThrows(EmptyDetailsException.class,()->landLordService.login(request));
    }

    @Test void testThatLandlordCannotLoginWithoutInputtingPassword(){
        LoginRequest request = loginDetails("landlord@gmail.com","");
        assertThrows(EmptyDetailsException.class,()->landLordService.login(request));
    }

    @Test void testThatAUserCanCompleteRegistrationAfterRegistering() throws NumberParseException, IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","Abuja",1L, "FirstBank","11111");

        File file = new File("/home/user/Pictures/flower.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.completeRegistration(request,multipartFile);

    }

    @Test void testThatAUserCanCompleteRegistrationAfterRegistering2() throws NumberParseException, IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","Abuja",2L, "Globus","121212");

        File file = new File("/home/user/Pictures/flower.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.completeRegistration(request,multipartFile);

    }

    @Test void testThatStateOfOriginFieldCannotBeEmpty() throws IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","09062346551","",2L,"Oceanic","22222");
        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }

    @Test void testThatPhoneNumberFieldCannotBeEmpty() throws IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("Lagos","","Ilorin",2L,"Palmpay","33333");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }

    @Test void testThatLocationFieldCannotBeEmpty() throws IOException {
        CompleteRegistrationRequest request = completeRegistrationRequest("","09062346551","Ilorin",2L,"Gtco","4444444");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(EmptyDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }

    @Test void testThatLandlordCannotCompleteProfileWithPhoneNumberThatDidNotMatchTheVerification() throws IOException {

        CompleteRegistrationRequest request = completeRegistrationRequest("Kwara","090765488900000","Ilorin",2L,"Zenith","55555");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        assertThrows(InvalidDetailsException.class,()->landLordService.completeRegistration(request,multipartFile));
    }




    @Test void postApartment() throws IOException {
        CreatePostRequest request = new CreatePostRequest();
        request.setLandLordId(1L);
        request.setDescription("Studio2");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("MINIFLAT"));
        request.setAnnualRentFee("150000");
        request.setAgreementAndCommission("10000");

        File file = new File("/home/user/Pictures/houseLogo.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        CreatePostResponse response =landLordService.post(request,multipartFile);
        System.out.println(response.getId());
        assertThat(response).isNotNull();
    }


    @Test void postApartment2() throws IOException {
        CreatePostRequest request = new CreatePostRequest();
        request.setLandLordId(1L);
        request.setDescription("fine house");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("SELFCONTAINED"));
        request.setAnnualRentFee("6050000");
        request.setAgreementAndCommission("80000");

        File file = new File("/home/user/Pictures/house1.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        CreatePostResponse response =landLordService.post(request,multipartFile);
        System.out.println(response.getId());
        assertThat(response).isNotNull();
    }

    @Test void postApartment3() throws IOException {
        CreatePostRequest request = new CreatePostRequest();
        request.setLandLordId(1L);
        request.setDescription("## A Serene Urban Haven: The Perfect Apartment\n" +
                "\n" +
                "Nestled in the heart of a bustling city, this charming apartment offers the ideal blend of modern convenience and serene comfort. Designed with a keen eye for detail and an emphasis on functionality, it provides a sanctuary for those seeking a peaceful retreat amidst the urban landscape.\n" +
                "\n" +
                "### Location\n" +
                "\n" +
                "Located in a prime area, the apartment is just a stone’s throw away from essential amenities and vibrant city life. With a variety of cafes, restaurants, and shops lining the streets, everything you need is within walking distance. Excellent public transportation options, including buses and subway lines, ensure that the entire city is easily accessible, making commuting a breeze.\n" +
                "\n" +
                "### Living Space\n" +
                "\n" +
                "The apartment features an open-concept design, creating a spacious and airy atmosphere. As you step inside, you are greeted by a bright and welcoming living room with large windows that allow natural light to flood the space. The neutral color palette of the walls and flooring enhances the feeling of openness and provides a blank canvas for personalization.\n" +
                "\n" +
                "### Kitchen\n" +
                "\n" +
                "Adjacent to the living room is a sleek, modern kitchen equipped with state-of-the-art appliances. The kitchen boasts ample counter space and cabinetry, making it a joy for both everyday cooking and entertaining guests. The stainless steel appliances, including a refrigerator, oven, and dishwasher, add a touch of sophistication and ensure that all your culinary needs are met.\n" +
                "\n" +
                "### Bedrooms\n" +
                "\n" +
                "The apartment includes two well-appointed bedrooms, each offering a tranquil retreat from the hustle and bustle of city life. The master bedroom features a spacious layout with a large closet, ensuring plenty of storage space. The second bedroom can serve as a guest room, home office, or a cozy space for relaxation. Both rooms are designed with comfort in mind, providing a serene environment for restful nights.\n" +
                "\n" +
                "### Bathroom\n" +
                "\n" +
                "The modern bathroom is a perfect blend of style and functionality. It features contemporary fixtures, a sleek vanity, and a large mirror that adds a sense of space. The bathtub with a shower offers a relaxing escape after a long day, and the high-quality finishes throughout add a touch of luxury.\n" +
                "\n" +
                "### Additional Features\n" +
                "\n" +
                "This apartment goes above and beyond with additional features that enhance the overall living experience. A private balcony provides an outdoor space where you can enjoy morning coffee or unwind with a book. In-unit laundry facilities offer convenience and eliminate the need for trips to the laundromat. Additionally, the building includes amenities such as a fitness center, a rooftop terrace, and secure parking, ensuring that all your lifestyle needs are catered to.\n" +
                "\n" +
                "### Community\n" +
                "\n" +
                "Living in this apartment also means becoming part of a friendly and welcoming community. The building management organizes regular events and gatherings, fostering a sense of camaraderie among residents. Whether it’s a rooftop barbecue or a yoga session in the fitness center, there are plenty of opportunities to connect with neighbors and make new friends.\n" +
                "\n" +
                "### Conclusion\n" +
                "\n" +
                "This apartment is more than just a place to live; it’s a lifestyle choice. With its prime location, modern amenities, and thoughtful design, it offers the perfect balance of urban excitement and peaceful retreat. Whether you’re a young professional, a couple, or a small family, this apartment provides a beautiful and comfortable space to call home. Experience the best of city living in a serene and stylish setting – your perfect urban haven awaits.");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("TWOBEDROOM"));
        request.setAnnualRentFee("6050000");
        request.setAgreementAndCommission("80000");

        File file = new File("/home/user/Pictures/house9.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        CreatePostResponse response =landLordService.post(request,multipartFile);
        System.out.println(response.getId());
        assertThat(response).isNotNull();
    }
    
    @Test void testDeleteApartment(){


        DeleteApartmentResponse2 response = landLordService.deleteApartment2(16L);
        assertThat(response).isNotNull();
        log.info("{}->",response);
    }
    @Test void testThatAUserCanRegister() throws NumberParseException {
        RegisterLandLordRequest request = landlordDetails("Adamu","Musa","joy@gmail.com","PassKey@123");
        AuthenticationResponse response = landLordService.register(request);
        log.info("{}",response);
        assertThat(response).isNotNull();
    }



  @Test
  public void findApartment(){

      System.out.println(userRepository.findById(5L));

  }

  @Test void addToApartmentGallery() throws IOException {
      File file = new File("/home/user/Pictures/house4.jpeg");
      FileInputStream inputStream = new FileInputStream(file);
      MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

    landLordService.addMoreImageToApartment(multipartFile,6L);
  }
    @Test void addMultipleImagesToApartmentGallery() throws IOException {
        File file = new File("/home/user/Pictures/house3.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.addMoreImageToApartment(multipartFile,6L);

        File file2 = new File("/home/user/Pictures/house2.jpeg");
        FileInputStream inputStream2 = new FileInputStream(file2);
        MultipartFile multipartFile2 = new MockMultipartFile("filename",inputStream2);

        landLordService.addMoreImageToApartment(multipartFile2,6L);
    }

    @Test void addMultipleImagesToApartmentGallery2() throws IOException {
        File file = new File("/home/user/Pictures/house1.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.addMoreImageToApartment(multipartFile,6L);

        File file2 = new File("/home/user/Pictures/house4.jpeg");
        FileInputStream inputStream2 = new FileInputStream(file2);
        MultipartFile multipartFile2 = new MockMultipartFile("filename",inputStream2);

        landLordService.addMoreImageToApartment(multipartFile2,6L);
    }

    @Test void addMultipleImagesToApartmentGallery3() throws IOException {
        File file = new File("/home/user/Pictures/house5.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);

        landLordService.addMoreImageToApartment(multipartFile,6L);

        File file2 = new File("/home/user/Pictures/house6.jpeg");
        FileInputStream inputStream2 = new FileInputStream(file2);
        MultipartFile multipartFile2 = new MockMultipartFile("filename",inputStream2);

        landLordService.addMoreImageToApartment(multipartFile2,6L);
    }

    @Test void findGalleryImages(){
        System.out.println(landLordService.findUserById(1L).getApartment2s());
    }
//    @Test
//    public void updateLandLordApartmentDetailsTest(){
//        UpdateLandLordApartmentRequest request = new UpdateLandLordApartmentRequest();
////        request.setHouseType("MINIFLAT");
//        request.setLocation("313, Herbert Macaulay tyyxxc , Sabo-Yaba");
//
//
//        ApiResponse<UpdateLandLordResponse> response =
//                landLordService.updateLandLordApartmentDetails(2L,1L,request);
//
//        System.out.println(request);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getData().getMessage()).isNotNull();
//
//    }






}
