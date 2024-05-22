package com.semicolon.campusnestproject.data.repositories;

import com.semicolon.campusnestproject.data.model.LandLord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandLordRepository extends JpaRepository<LandLord,Long> {
}
