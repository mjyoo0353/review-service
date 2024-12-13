package com.hanghae.reviewservice.service;

import com.hanghae.reviewservice.dto.ReviewListResponseDto;
import com.hanghae.reviewservice.dto.ReviewRequestDto;
import com.hanghae.reviewservice.dto.ReviewResponseDto;
import com.hanghae.reviewservice.entity.Product;
import com.hanghae.reviewservice.entity.Review;
import com.hanghae.reviewservice.repository.ProductRepository;
import com.hanghae.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    //상품에 대한 리뷰 등록
    @Transactional
    public ReviewResponseDto createReview(Long productId, ReviewRequestDto requestDto, MultipartFile imageFile) {
        Product product = getProductOrThrowException(productId);

        // 유저가 상품에 대해 리뷰를 작성했는지 확인하고 이미 작성한 리뷰가 있다면 예외처리
        boolean isExistReview = reviewRepository.existsByUserIdAndProductId(requestDto.getUserId(), productId);
        if (isExistReview) throw new IllegalArgumentException("하나의 상품에 대해 하나의 리뷰만 작성이 가능합니다.");

        //이미지 파일 처리
        String imageUrl = s3Service.uploadImage(imageFile);

        Review review = new Review(requestDto, product, imageUrl);
        reviewRepository.save(review);

        // 리뷰 통계 업데이트 (상품 리뷰 개수, 평균 점수 갱신)
        updateProductReviewStats(product);
        productRepository.save(product);

        return new ReviewResponseDto(review);
    }

    //상품에 대한 리뷰 조회
    public ReviewListResponseDto getReviewList(Long productId, Long cursor, int size) {
        Product product = getProductOrThrowException(productId);

        //productId와 관련된 모든 리뷰 데이터를 reviewList에 저장
        List<Review> reviewList;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Order.desc("createdAt")));
        if (cursor == 0) {
            reviewList = reviewRepository.findAllByProductIdOrderByCreatedAtDesc(productId, pageable);
        } else {
            reviewList = reviewRepository.findAllByProductIdAndIdLessThanOrderByCreatedAtDesc(productId, cursor, pageable);
        }

        //리뷰 리스트가 없다면 예외처리
        if (reviewList.isEmpty()) {
            throw new IllegalArgumentException("해당 상품에 대한 리뷰가 존재하지 않습니다.");
        }

        //총 리뷰 수와 평균 점수 계산
        Long totalReviewCount = reviewRepository.countByProductId(productId);
        float averageScore = reviewRepository.calculateAverageScoreByProductId(productId);

        //List<Review>를 List<ReviewResponseDto>로 변환
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (Review review : reviewList) {
            reviewResponseDtoList.add(new ReviewResponseDto(review));
        }

        //마지막 리뷰의 ID를 cursor로 설정하여 페이징에 활용
        Long newCursor = reviewList.get(reviewList.size() - 1).getId();

        return new ReviewListResponseDto(reviewResponseDtoList, totalReviewCount, averageScore, newCursor);
    }

    private Product getProductOrThrowException(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));
    }

    public void updateProductReviewStats(Product product) {
        //리뷰 등록 시, 해당 상품에 대한 리뷰 수 증가 및 평균 점수 계산
        Long newReviewCount = reviewRepository.countByProductId(product.getId());
        float newAverageScore = reviewRepository.calculateAverageScoreByProductId(product.getId());

        product.setReviewCount(newReviewCount);
        product.setScore(newAverageScore);
    }

}
