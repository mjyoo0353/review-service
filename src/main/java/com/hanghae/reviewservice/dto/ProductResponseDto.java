package com.hanghae.reviewservice.dto;

import com.hanghae.reviewservice.entity.Product;
import lombok.Getter;


@Getter
public class ProductResponseDto {
    private Long id;
    private Long reviewCount;
    private double score;

    public ProductResponseDto(Product savedProduct) {
        this.id = savedProduct.getId();
        this.reviewCount = savedProduct.getReviewCount();
        this.score = savedProduct.getScore();
    }
}
