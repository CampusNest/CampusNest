package com.semicolon.campusnestproject.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity

public class Apartment2 {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private ApartmentType apartmentType;
    private String annualRentFee;
    private String agreementAndCommission;
    private String location;
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
}
