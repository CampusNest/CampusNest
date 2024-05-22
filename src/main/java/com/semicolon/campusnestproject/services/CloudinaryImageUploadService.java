package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.requests.UploadApartmentImageRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudinaryImageUploadService {

    UploadApartmentImageResponse uploadImage(UploadApartmentImageRequest request) throws IOException;

}
