package com.semicolon.campusnestproject.dtos.requests;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class UploadApartmentImageRequest {

    private List<MultipartFile> multipartFiles ;
}
