package com.hanghae.reviewservice.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


@Getter
public class ReviewRequestDto {
    private Long userId;
    private String content;
    private double score; //리뷰 점수 (1-5)
    private MultipartFile imageUrl; //Optional
}
