package com.semicolon.campusnestproject.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semicolon.campusnestproject.dtos.requests.UploadApartmentImageRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.services.CloudinaryImageUploadService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CampusNextCloudinaryImageUploadService implements CloudinaryImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public UploadApartmentImageResponse uploadImage(UploadApartmentImageRequest request) throws IOException {
        UploadApartmentImageResponse response = new UploadApartmentImageResponse();
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : request.getMultipartFiles()){
            File convertedFile = convert(file);
            Map uploadedResult = cloudinary.uploader().upload(convertedFile, ObjectUtils.emptyMap());
            String  url = uploadedResult.get("url").toString();
            urls.add(url);
        }
        response.setImageUrl(urls);
        return response;
    }

    private File convert(MultipartFile file) throws IOException {
        File newFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return newFile;
    }

}
