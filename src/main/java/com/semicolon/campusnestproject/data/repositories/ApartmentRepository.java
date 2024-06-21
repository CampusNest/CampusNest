package com.semicolon.campusnestproject.data.repositories;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import com.semicolon.campusnestproject.data.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment,Long> {
    List<Apartment> findByApartmentType(ApartmentType apartmentType);



}
