
package com.hanghae.reviewservice.dto;

import lombok.Getter;
import java.util.List;


@Getter
public class ReviewListResponseDto {
    private Long totalReviewCount;
    private double averageScore; //리뷰 평균 점수 (1-5)
    private Long cursor; //위치
    private List<ReviewResponseDto> reviewList;

    public ReviewListResponseDto(List<ReviewResponseDto> reviewList, Long totalReviewCount, double averageScore, Long cursor) {
        this.reviewList = reviewList;
        this.totalReviewCount = totalReviewCount;
        this.averageScore = averageScore;
        this.cursor = cursor;
    }

}

