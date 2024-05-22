package com.semicolon.campusnestproject.services.implementations;

import com.semicolon.campusnestproject.data.model.LandLord;
import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentRequest;
import com.semicolon.campusnestproject.services.LandLordService;
import jakarta.persistence.metamodel.IdentifiableType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CampusNestLandLordService implements LandLordService {

    @Override
    public Optional<LandLord> findByEmail(String email) {
        return null;
    }

    @Override
    public void updateApartmentDescription(Long landLordId, UpdateApartmentRequest updateApartmentRequest) {

    }
}
