package com.semicolon.campusnestproject.services;


import com.google.i18n.phonenumbers.NumberParseException;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.dtos.requests.*;
import com.semicolon.campusnestproject.dtos.responses.AuthenticationResponse;
import com.semicolon.campusnestproject.dtos.responses.PostApartmentResponse;
import com.semicolon.campusnestproject.exception.InvalidDetailsException;
import com.semicolon.campusnestproject.exception.UserExistException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
public class LandLordServiceTest {

    @Autowired
    private LandLordService landLordService;




    @Test
    public void postApartmentTest() throws IOException {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        PostApartmentRequest request = new PostApartmentRequest();
        request.setLandLordId(1L);
        request.setDescription("Fine House");
        request.setLocation("Yaba");
        request.setApartmentType(ApartmentType.valueOf("MINIFLAT"));
        request.setAnnualRentFee("150000");
        request.setAgreementAndCommission("10000");
        UploadApartmentImageRequest imageRequest = new UploadApartmentImageRequest();
        File file = new File("C:\\Users\\USER\\Pictures\\1char.png");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("filename",inputStream);
        multipartFiles.add(multipartFile);
        imageRequest.setMultipartFiles(multipartFiles);
        request.setUploadApartmentImageRequest(imageRequest);
        PostApartmentResponse response = landLordService.postApartment(request);
        assertThat(response).isNotNull();
    }
}
