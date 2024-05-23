package com.semicolon.campusnestproject.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.dtos.requests.UploadApartmentImageRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.services.CloudinaryImageUploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CampusNestCloudinaryImageUploadService implements CloudinaryImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public UploadApartmentImageResponse uploadImage(UploadApartmentImageRequest request) throws IOException {
        UploadApartmentImageResponse response = new UploadApartmentImageResponse();
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : request.getMultipartFiles()){
            Map uploadedResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String  url = uploadedResult.get("url").toString();
            urls.add(url);
        }
        response.setImageUrl(urls);
        return response;
    }

    @Override
    public void deleteImage(List<Image> images) throws IOException {
        String url;
        for (Image image : images){
            url = image.getImageUrl();
            String publicId = extractPublicIdFromUrl(url);
            cloudinary.uploader().destroy(publicId,ObjectUtils.emptyMap());
        }
    }

    private static String extractPublicIdFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] segments = path.split("/");
            String publicIdWithExtension = segments[segments.length - 1];
            return publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
        } catch (Exception e) {
            throw new CampusNestException(e.getMessage());
        }
    }

}
