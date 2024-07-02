package com.semicolon.campusnestproject.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteGalleryRequest {
    private String imageUrl;
    private Long apartmentId;
}
