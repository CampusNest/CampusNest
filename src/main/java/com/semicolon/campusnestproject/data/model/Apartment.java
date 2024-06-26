package com.semicolon.campusnestproject.data.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@ToString
public class Apartment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private ApartmentType apartmentType;
    private String annualRentFee;
    private String agreementAndCommission;
    private String location;
    @OneToMany(fetch = FetchType.EAGER, cascade = {PERSIST,DETACH,REMOVE})
    private List<Image> apartmentImage;

}


