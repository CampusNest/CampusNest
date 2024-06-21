package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.data.model.Image2;
import com.semicolon.campusnestproject.dtos.requests.UploadApartmentImageRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;

import java.io.IOException;
import java.util.List;

public interface CloudinaryImageUploadService {

    UploadApartmentImageResponse uploadImage(UploadApartmentImageRequest request) throws IOException;

//    void deleteImage(List<Image> images) throws IOException;

    void deleteImage(String images) throws IOException;
}
