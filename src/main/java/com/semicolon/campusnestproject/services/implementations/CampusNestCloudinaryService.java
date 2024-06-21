package com.semicolon.campusnestproject.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semicolon.campusnestproject.data.model.Image2;
import com.semicolon.campusnestproject.data.repositories.ImageRepository;
import com.semicolon.campusnestproject.data.repositories.ImageRepository2;
import com.semicolon.campusnestproject.dtos.responses.UploadImageResponse;
import com.semicolon.campusnestproject.services.CloudinaryService2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CampusNestCloudinaryService implements CloudinaryService2 {
    private final Cloudinary cloudinary;
    private final ImageRepository2 imageRepository2;



    @Override
    public UploadImageResponse uploadImage(MultipartFile multipartFile) throws IOException {

        Map uploadedFile = cloudinary.uploader()
                .upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        saveFile(uploadedFile);

        UploadImageResponse uploadImageResponse = new UploadImageResponse();
        uploadImageResponse.setImageUrl(uploadedFile.get("url").toString());

        return uploadImageResponse;
    }

    private void saveFile(Map uploadedFile) {
        Image2 image = Image2.builder()
//                .user(userService.findBY(uploadImageRequest.getUserId()))
                .url(uploadedFile.get("url").toString())
                .build();
        imageRepository2.save(image);
    }


}
