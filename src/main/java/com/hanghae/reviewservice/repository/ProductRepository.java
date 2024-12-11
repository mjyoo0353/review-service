package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("update Product p set p.reviewCount = :reviewCount, p.score = :score where p.id = :productId")
    void updateReviewCountAndScore(@Param("productId") Long productId,
                                   @Param("reviewCount") Long reviewCount,
                                   @Param("score") double score);
}
