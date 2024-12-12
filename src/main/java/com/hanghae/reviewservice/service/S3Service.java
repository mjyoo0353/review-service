package com.hanghae.reviewservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    //dummy url 반환 메서드 (S3에 저장된다고 가정)
    public String uploadImage(MultipartFile imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        System.out.println("더미 S3에 이미지 업로드: " + imageUrl.getOriginalFilename());
        return "https://dummy-s3-url/" + imageUrl.getOriginalFilename();
    }
}
