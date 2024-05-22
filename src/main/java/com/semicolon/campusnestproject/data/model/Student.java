package com.semicolon.campusnestproject.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
public class Student {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String stateOfOrigin;
    private String phoneNumber;
    private String location;
    @Enumerated(EnumType.STRING)
    private Role role;
}
