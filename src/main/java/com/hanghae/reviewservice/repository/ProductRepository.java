package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //비관적 락을 사용하여 상품 정보를 읽을 때 락을 걸어, 상품 정보를 동시에 수정할 수 없게 함
    //리뷰 등록 시, 상품의 reviewCount와 averageScore를 동기화하여 안전하게 업데이트
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Optional<Product> findById(Long id);

    @Modifying
    @Query("update Product p set p.reviewCount = :reviewCount, p.score = :score where p.id = :productId")
    void updateReviewCountAndScore(@Param("productId") Long productId,
                                   @Param("reviewCount") Long reviewCount,
                                   @Param("score") double score);
}
