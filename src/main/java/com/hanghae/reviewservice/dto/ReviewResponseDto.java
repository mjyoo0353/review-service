package com.hanghae.reviewservice.dto;

import com.hanghae.reviewservice.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private double score; //리뷰 점수 (1-5)
    private String imageUrl;
    private LocalDateTime createdAt = LocalDateTime.now();

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.userId = review.getUserId();
        this.content = review.getContent();
        this.score = review.getScore();
        this.imageUrl = review.getImageUrl();
        this.createdAt = review.getCreatedAt();
    }
}
