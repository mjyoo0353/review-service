package com.hanghae.reviewservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class ReviewRequestDto {
    private Long userId;
    private String content;
    private float score; //리뷰 점수 (1-5)
    private MultipartFile imageUrl; //Optional
}
