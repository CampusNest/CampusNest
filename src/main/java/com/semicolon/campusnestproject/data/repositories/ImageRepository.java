package com.semicolon.campusnestproject.data.repositories;

import com.semicolon.campusnestproject.data.model.Image;
import com.semicolon.campusnestproject.data.model.Image2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
