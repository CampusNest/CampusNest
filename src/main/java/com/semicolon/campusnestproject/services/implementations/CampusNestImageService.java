package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.data.repositories.ImageRepository;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CampusNestImageService implements ImageService {

    private final ImageRepository imageRepository;
    @Override
    public List<Image> SaveImage(UploadApartmentImageResponse imageRequest) {
        Image image = new Image();
        List<Image> images = new ArrayList<>();
        for (String url : imageRequest.getImageUrl()){
            image.setImageUrl(url);
        }
        imageRepository.save(image);
        images.add(image);
        return images;
    }

    @Override
    public void deleteImage(List<Image> images) {
        imageRepository.deleteAll(images);
    }
}
