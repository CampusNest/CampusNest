package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.*;
import com.semicolon.campusnestproject.data.repositories.ApartmentRepository;
import com.semicolon.campusnestproject.data.repositories.ApartmentRepository2;
import com.semicolon.campusnestproject.data.repositories.UserRepository;
import com.semicolon.campusnestproject.dtos.requests.PostApartmentRequest;
import com.semicolon.campusnestproject.dtos.responses.UploadApartmentImageResponse;
import com.semicolon.campusnestproject.exception.CampusNestException;
import com.semicolon.campusnestproject.exception.UserNotFoundException;
import com.semicolon.campusnestproject.services.ApartmentService;
import com.semicolon.campusnestproject.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CampusNestApartmentService implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentRepository2 apartmentRepository2;
    private final ImageService imageService;
    private final UserRepository userRepository;

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
    public void deleteApartment( Long apartmentId) {
        apartmentRepository.deleteById(apartmentId);
    }

//    @Override
//    public List<Image> getApartmentImage(Long apartmentId) {
//        Optional<Apartment> apartment = apartmentRepository.findById(apartmentId);
//        return apartment.get().getApartmentImage();
//    }
@Override
public String getApartmentImage(Long apartmentId) {
    Optional<Apartment2> apartment = apartmentRepository2.findById(apartmentId);

    if (apartment.isEmpty()){
        throw new CampusNestException("apartment image not found");
    }
    return apartment.get().getImage();
}


    @Override
    public Apartment deleteImageFromApartment(Long apartmentId) {
        Optional<Apartment> apartment = apartmentRepository.findById(apartmentId);
        List<Image> images = apartment.get().getApartmentImage();
        Iterator<Image> iterator = images.iterator();
        while (iterator.hasNext()) {
            Image image = iterator.next();
            iterator.remove();
        }
        apartmentRepository.save(apartment.get());
        return apartment.get();
    }

    @Override
    public Optional<Apartment2> getApartment(Long apartmentId) {
        return apartmentRepository2.findById(apartmentId);
    }

    @Override
    public Apartment findApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId).get();
    }

    @Override
    public List<Apartment> findApartmentByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("{\"error\" : \"user not found\"}"));

        return user.getApartments();
    }

    @Override
    public List<Apartment2> findApartmentUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("{\"error\" : \"user not found\"}"));

        return user.getApartment2s();
    }


    @Override
    public List<Apartment> allApartment() {
        return apartmentRepository.findAll();
    }

    @Override
    public Long getLandLord(Long apartmentId) {
       Long  user = findLandLordApartment(apartmentId);

       if (user == null){
           throw new UserNotFoundException("user not found");
       }

       return user;
    }



    public Long findLandLordApartment(Long apartmentId){
        List<User> users = userRepository.findAll();


        for (User user : users){
            for (Apartment apartment : user.getApartments()){
                if (apartment.getId() == apartmentId){
                    return user.getId();
                }

            }
        }

        return null;
    }

//    @Override
//    public List<Apartment> allApartment() {
//        List<Apartment> apartments = apartmentRepository.findAll();
//
//        for (Apartment apartment : apartments){
//           BigDecimal money =  new BigDecimal(apartment.getAnnualRentFee()).add(BigDecimal.valueOf(7000));
//           apartment.setAnnualRentFee(money.toString());
//            apartmentRepository.save(apartment);
//        }
//
//        return apartmentRepository.findAll();
//
//    }

    @Override
    public Apartment findById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("landlord with id %d not found", apartmentId)));
    }

    @Override
    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }





}
