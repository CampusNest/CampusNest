package com.semicolon.campusnestproject.data.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String imageUrl;
}
