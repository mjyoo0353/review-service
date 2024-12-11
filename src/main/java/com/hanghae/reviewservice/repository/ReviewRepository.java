package com.hanghae.reviewservice.repository;

import com.hanghae.reviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

}
