package com.semicolon.campusnestproject.dtos.responses;


import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class UploadApartmentImageResponse {

    private List<String> imageUrl;
}
