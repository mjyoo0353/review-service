package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    //해당 유저가 해당 상품에 대한 리뷰를 작성했는지 확인
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
