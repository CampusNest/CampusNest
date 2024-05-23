package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.data.repositories.ApartmentRepository;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CampusNestApartmentService implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ImageService imageService;

    @Override
    public List<Apartment> findApartmentBy(String apartmentType) {
        return apartmentRepository.findByApartmentType(ApartmentType.valueOf(apartmentType));
    }



    @Override
    public Apartment saveApartment(PostApartmentRequest request, UploadApartmentImageResponse imageRequest) {
        Apartment apartment = new Apartment();
        apartment.setApartmentType(request.getApartmentType());
        apartment.setDescription(request.getDescription());
        apartment.setLocation(request.getLocation());
        apartment.setAgreementAndCommission(request.getAgreementAndCommission());
        apartment.setAnnualRentFee(request.getAnnualRentFee());
        apartmentRepository.save(apartment);
        List<Image> images = imageService.SaveImage(imageRequest);
        Optional<Apartment> apartment1 = apartmentRepository.findById(apartment.getId());
        apartment1.get().setApartmentImage(images);
        apartmentRepository.save(apartment1.get());
        return apartment;
    }




    @Override
    public void deleteApartment(List<Apartment> apartments, Long apartmentId) {
        for (Apartment apartment : apartments){
            if (Objects.equals(apartment.getId(), apartmentId)){
                apartmentRepository.delete(apartment);
                break;
            }
        }
    }

    @Override
    public List<Image> getApartmentImage(List<Apartment> apartments, Long apartmentId) {
        List<Image> images = new ArrayList<>();
        for (Apartment apartment : apartments){
            if (Objects.equals(apartment.getId(), apartmentId)){
                images = apartment.getApartmentImage();
                break;
            }
        }
        return images;
    }
}
