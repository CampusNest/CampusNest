package com.semicolon.campusnestproject.data.repositories;

import com.semicolon.campusnestproject.data.model.Apartment;
import com.semicolon.campusnestproject.data.model.ApartmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment,Long> {
    List<Apartment> findByApartmentType(ApartmentType apartmentType);
}
