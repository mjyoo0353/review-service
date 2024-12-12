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
    private final ProductService productService;

    //상품에 대한 리뷰 등록
    public ReviewResponseDto createReview(Long productId, ReviewRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));

        // 유저가 상품에 대해 리뷰를 작성했는지 확인하고 이미 작성한 리뷰가 있다면 예외처리
        boolean isExistReview = reviewRepository.existsByUserIdAndProductId(requestDto.getUserId(), productId);
        if (isExistReview) {
            throw new IllegalArgumentException("하나의 상품에 대해 하나의 리뷰만 작성이 가능합니다.");
        }

        Review review = new Review(requestDto, product);

        reviewRepository.save(review);
        productRepository.save(product);

        //리뷰 등록 시 리뷰 수 증가 및 평균 점수 업데이트
        Long totalReviewCount = product.getReviewCount() + 1;
        double newAverageScore = calculateAverageScore(product.getScore(), product.getReviewCount(), requestDto.getScore());

        productService.updateReviewCountAndScore(productId, totalReviewCount, newAverageScore);

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
        return reviewResponseDto;
    }

    //새로운 평균 점수 계산
    private double calculateAverageScore(double averageScore, Long totalReviewCount, double newScore) {
        return (averageScore * totalReviewCount + newScore) / (totalReviewCount + 1);
    }

}
