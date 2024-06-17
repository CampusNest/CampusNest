package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.dtos.responses.UploadImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService2 {
    UploadImageResponse uploadImage(MultipartFile multipartFile) throws IOException;
}
