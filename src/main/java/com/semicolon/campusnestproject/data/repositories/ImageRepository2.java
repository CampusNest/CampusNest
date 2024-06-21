package com.semicolon.campusnestproject.data.repositories;

import com.semicolon.campusnestproject.data.model.Image2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository2 extends JpaRepository<Image2,Long> {
    Image2 findImage2ByUrl(String url);


}
