package com.semicolon.campusnestproject.services;

import com.semicolon.campusnestproject.data.model.LandLord;
import com.semicolon.campusnestproject.dtos.requests.UpdateApartmentRequest;
import jakarta.persistence.metamodel.IdentifiableType;

import java.util.Optional;

public interface LandLordService {

    Optional<LandLord> findByEmail(String email);

    void updateApartmentDescription(Long landLordId, UpdateApartmentRequest updateApartmentRequest);
}
