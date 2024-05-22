package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;

import java.util.List;

public interface ImageService {
    List<Image> SaveImage(UploadApartmentImageResponse imageRequest);
}
