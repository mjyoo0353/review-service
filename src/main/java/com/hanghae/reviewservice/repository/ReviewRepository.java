package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    //해당 유저가 해당 상품에 대한 리뷰를 작성했는지 확인
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    //해당 상품의 리뷰 리스트 조회
    List<Review> findAllByProductIdAndIdGreaterThanOrderByCreatedAtDesc(Long productId, Long cursor, Pageable pageable);
}
