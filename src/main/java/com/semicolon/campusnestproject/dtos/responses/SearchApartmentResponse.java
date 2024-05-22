package com.semicolon.campusnestproject.dtos.responses;


import com.semicolon.campusnestproject.data.model.Apartment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchApartmentResponse {

    private List<Apartment> apartments;
}
