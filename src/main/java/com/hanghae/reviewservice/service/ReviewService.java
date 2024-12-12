package com.hanghae.reviewservice.service;

import com.hanghae.reviewservice.dto.ReviewListResponseDto;
import com.hanghae.reviewservice.dto.ReviewRequestDto;
import com.hanghae.reviewservice.dto.ReviewResponseDto;
import com.hanghae.reviewservice.entity.Product;
import com.hanghae.reviewservice.entity.Review;
import com.hanghae.reviewservice.repository.ProductRepository;
import com.hanghae.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final S3Service s3Service;

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

    //상품에 대한 리뷰 조회
    public ReviewListResponseDto getReviewList(Long productId) {
        //productId와 관련된 모든 리뷰 데이터를 reviewList에 저장
        List<Review> reviewList = reviewRepository.findAllByProductIdOrderByCreatedAtDesc(productId);

        //리뷰 리스트가 없다면 예외처리
        if (reviewList.isEmpty()) {
            throw new IllegalArgumentException("해당 상품에 대한 리뷰가 존재하지 않습니다.");
        }

        //리뷰 리스트의 총 개수와 평균 점수 계산
        Long totalReviewCount = (long) reviewList.size();
        double totalScore = 0.0;
        for (Review review : reviewList) {
            totalScore += review.getScore();
        }
        double averageScore = totalScore / totalReviewCount;

        //List<Review>를 List<ReviewResponseDto>로 변환
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for (Review review : reviewList) {
            reviewResponseDtos.add(new ReviewResponseDto(review));
        }

        ReviewListResponseDto reviewListResponseDto = new ReviewListResponseDto(reviewResponseDtos, totalReviewCount, averageScore);
        return reviewListResponseDto;
    }

    //새로운 평균 점수 계산
    private double calculateAverageScore(double averageScore, Long totalReviewCount, double newScore) {
        return (averageScore * totalReviewCount + newScore) / (totalReviewCount + 1);
    }

}
