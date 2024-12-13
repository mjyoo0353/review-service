package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    //해당 유저가 해당 상품에 대한 리뷰를 작성했는지 확인
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    //해당 상품에 대한 리뷰 개수 조회
    Long countByProductId(Long productId);

    // 특정 상품에 대한 평균 점수 계산
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.product.id = :productId")
    float calculateAverageScoreByProductId(@Param("productId") Long productId);

    //cursor 없이 해당 상품의 전체 리뷰 리스트 조회
    @Query("SELECT r From Review r WHERE r.product.id = :productId ORDER BY r.createdAt DESC")
    List<Review> findAllByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    //해당 상품의 리뷰 리스트 조회
    @Query("SELECT r From Review r WHERE r.product.id = :productId And r.id < :cursor ORDER BY r.createdAt DESC")
    List<Review> findAllByProductIdAndIdLessThanOrderByCreatedAtDesc(@Param("productId")Long productId,
                                                                        @Param("cursor") Long cursor,
                                                                        Pageable pageable);



}
