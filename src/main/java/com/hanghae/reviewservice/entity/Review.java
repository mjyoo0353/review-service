package com.hanghae.reviewservice.entity;

import com.hanghae.reviewservice.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private double score; //리뷰 점수 (1-5)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Review(ReviewRequestDto requestDto, Product product) {
        this.product = product;
        this.userId = requestDto.getUserId();
        this.content = requestDto.getContent();
        this.score = requestDto.getScore();
    }

}
