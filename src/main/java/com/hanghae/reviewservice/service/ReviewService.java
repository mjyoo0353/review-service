package com.hanghae.reviewservice.service;

import com.hanghae.reviewservice.dto.ReviewRequestDto;
import com.hanghae.reviewservice.dto.ReviewResponseDto;
import com.hanghae.reviewservice.entity.Product;
import com.hanghae.reviewservice.entity.Review;
import com.hanghae.reviewservice.repository.ProductRepository;
import com.hanghae.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    //상품에 대한 리뷰 등록
    public ReviewResponseDto createReview(Long productId, ReviewRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));

        Review review = new Review(requestDto, product);

        reviewRepository.save(review);
        productRepository.save(product);

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
        return reviewResponseDto;
    }

}
